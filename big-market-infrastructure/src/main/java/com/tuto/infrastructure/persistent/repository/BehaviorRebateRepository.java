package com.tuto.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson2.JSON;
import com.tuto.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.tuto.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.tuto.domain.rebate.model.entity.TaskEntity;
import com.tuto.domain.rebate.model.valobj.BehaviorTypeVO;
import com.tuto.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.tuto.domain.rebate.repository.IBehaviorRebateRepository;
import com.tuto.infrastructure.event.EventPublisher;
import com.tuto.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import com.tuto.infrastructure.persistent.dao.ITaskDao;
import com.tuto.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import com.tuto.infrastructure.persistent.po.DailyBehaviorRebate;
import com.tuto.infrastructure.persistent.po.Task;
import com.tuto.infrastructure.persistent.po.UserBehaviorRebateOrder;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 行为返利仓储实现
 * @author tu
 * @date 2024-12-30 下午9:45
 */
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {
    private static final Logger log = LoggerFactory.getLogger(BehaviorRebateRepository.class);
    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;


    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebateEntities = dailyBehaviorRebateDao.queryDailyBehaviorRebateConfig(behaviorTypeVO.getCode());
        if (dailyBehaviorRebateEntities != null && !dailyBehaviorRebateEntities.isEmpty()) {
            return dailyBehaviorRebateEntities.stream().map(dailyBehaviorRebateEntity ->
                    DailyBehaviorRebateVO.builder()
                            .behaviorType(dailyBehaviorRebateEntity.getRebateType())
                            .rebateConfig(dailyBehaviorRebateEntity.getRebateConfig())
                            .rebateDesc(dailyBehaviorRebateEntity.getRebateDesc())
                            .rebateType(dailyBehaviorRebateEntity.getRebateType())
                            .build()
            ).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();

                        // 用户行为返利订单
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setUserId(userId);
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        userBehaviorRebateOrder.setOutBusinessNo(behaviorRebateOrderEntity.getOutBusinessNo());
                        userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);

                        // 任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTask();
                        Task task = new Task();
                        task.setUserId(taskEntity.getUserId());
                        task.setTopic(taskEntity.getTopic());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        taskDao.insert(task);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入返利记录,唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(),ResponseCode.INDEX_DUP.getInfo(), e);
                }
            });
        }finally {
            dbRouter.clear();
        }

        // 同步发送 MQ消息
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            TaskEntity taskEntity = behaviorRebateAggregate.getTask();
            Task task = new Task();
            task.setUserId(userId);
            task.setMessageId(taskEntity.getMessageId());
            try {
                // 发送消息
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                // 更新数据库记录 task 任务表
                taskDao.updateTaskSendMessageCompleted(task);
            } catch (Exception e) {
                log.error("写入返利记录,发送 MQ 消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }
        }
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        // 1. 请求对象
        UserBehaviorRebateOrder userBehaviorRebateOrderReq = new UserBehaviorRebateOrder();
        userBehaviorRebateOrderReq.setUserId(userId);
        userBehaviorRebateOrderReq.setOutBusinessNo(outBusinessNo);
        // 2. 查询结果
        List<UserBehaviorRebateOrder> userBehaviorRebateOrders = userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrderReq);
        List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = new ArrayList<>();
        for (UserBehaviorRebateOrder userBehaviorRebateOrder : userBehaviorRebateOrders) {
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(userBehaviorRebateOrder.getUserId())
                    .orderId(userBehaviorRebateOrder.getOrderId())
                    .behaviorType(userBehaviorRebateOrder.getBehaviorType())
                    .rebateConfig(userBehaviorRebateOrder.getRebateConfig())
                    .rebateType(userBehaviorRebateOrder.getRebateType())
                    .rebateDesc(userBehaviorRebateOrder.getRebateDesc())
                    .bizId(userBehaviorRebateOrder.getBizId())
                    .outBusinessNo(userBehaviorRebateOrder.getOutBusinessNo())
                    .build();
            behaviorRebateOrderEntities.add(behaviorRebateOrderEntity);
        }
        return behaviorRebateOrderEntities;
    }
}
