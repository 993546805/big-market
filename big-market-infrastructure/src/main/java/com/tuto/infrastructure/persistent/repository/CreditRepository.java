package com.tuto.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import com.tuto.domain.credit.model.aggregate.TradeAggregate;
import com.tuto.domain.credit.model.entity.CreditAccountEntity;
import com.tuto.domain.credit.model.entity.CreditOrderEntity;
import com.tuto.domain.credit.model.entity.TaskEntity;
import com.tuto.domain.credit.repository.ICreditRepository;
import com.tuto.infrastructure.event.EventPublisher;
import com.tuto.infrastructure.persistent.dao.ITaskDao;
import com.tuto.infrastructure.persistent.dao.IUserCreditAccountDao;
import com.tuto.infrastructure.persistent.dao.IUserCreditOrderDao;
import com.tuto.infrastructure.persistent.po.Task;
import com.tuto.infrastructure.persistent.po.UserCreditAccount;
import com.tuto.infrastructure.persistent.po.UserCreditOrder;
import com.tuto.infrastructure.persistent.redis.IRedisService;
import com.tuto.types.common.Constants;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 用户积分仓储
 *
 * @author tu
 * @date 2025-01-06 下午9:35
 */
@Repository
public class CreditRepository implements ICreditRepository {

    private static final Logger log = LoggerFactory.getLogger(CreditRepository.class);
    @Resource
    private IRedisService redisService;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserCreditOrderDao userCreditOrderDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        // 积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        // 知识;仓储层往上有业务语义, 仓储往下到 dao 层操作是没有业务语义的,所以不用在乎这块使用的字段名称,直接用持久化对象即可.
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());
        userCreditOrderReq.setCreateTime(new Date());
        userCreditOrderReq.setUpdateTime(new Date());

        Task task = new Task();
        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getTaskState().getCode());


        RLock lock = redisService.getLock(Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOrderId());
        try {
            lock.lock(3, TimeUnit.SECONDS);
            dbRouter.doRouter(userId);
            // 编程式事务
            transactionTemplate.execute(status -> {
                try {
                    // 1. 保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (null == userCreditAccount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    } else {
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }
                    // 2. 保存账户订单
                    userCreditOrderDao.insert(userCreditOrderReq);
                    // 3. 写入任务
                    taskDao.insert(task);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常, 唯一索引冲突 userId: {} orderId: {}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;

            });
        } finally {
            lock.unlock();
            dbRouter.clear();
        }

        try {
            // 发送消息[在事务外执行,如果失败还有任务补偿]
            eventPublisher.publish(task.getTopic(), task.getMessage());
            // 更新数据库记录, task 任务表
            taskDao.updateTaskSendMessageCompleted(task);
            log.info("调整账户积分记录,发送 MQ消息完成 userId: {} orderId: {} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());

        } catch (Exception e) {
            log.error("调整账号积分记录,发送 MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }


}
