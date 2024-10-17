package com.tuto.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.tuto.domain.activity.model.aggregate.CreateOrderAggregate;
import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivityOrderEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;
import com.tuto.domain.activity.model.valobj.ActivityStateVO;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.infrastructure.persistent.dao.*;
import com.tuto.infrastructure.persistent.po.*;
import com.tuto.infrastructure.persistent.redis.IRedisService;
import com.tuto.infrastructure.persistent.redis.RedissonService;
import com.tuto.types.common.Constants;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author tu
 * @date 2024-10-17 11:22
 */
@Slf4j
@Repository
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;


    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryActivitySku(sku);
        if (null == raffleActivitySku) {
            return null;
        }
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();
    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        //  1.缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;

        // 2.数据库获取
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        if(null == raffleActivity) return null;
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        // 3.缓存
        redisService.setValue(cacheKey, activityEntity);

        return activityEntity;

    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) return activityCountEntity;
        // 从库中获取数据
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;
    }

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();

        // 订单对象
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId(createOrderAggregate.getUserId());
        raffleActivityOrder.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityOrder.setActivityName(activityOrderEntity.getActivityName());
        raffleActivityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        raffleActivityOrder.setOrderId(activityOrderEntity.getOrderId());
        raffleActivityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        raffleActivityOrder.setState(activityOrderEntity.getState().getCode());
        raffleActivityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());
        raffleActivityOrder.setCreateTime(new Date());
        raffleActivityOrder.setUpdateTime(new Date());

        //账户对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());


        // 以用户ID作为切分键，通过 doRouter 设定路由【这样就保证了下面的操作，都是同一个链接下，也就保证了事务的特性】
        dbRouter.doRouter(createOrderAggregate.getUserId());
        transactionTemplate.execute(status ->{
            try {
                // 1.写入订单
                raffleActivityOrderDao.insert(raffleActivityOrder);
                // 2. 更新账户
                int count = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                // 3. 创建账户 - 更新为0，则账户不存在，创新新账户。
                if (0 == count) {
                    raffleActivityAccountDao.insert(raffleActivityAccount);
                }
                return 1;
            } catch (DuplicateKeyException e) {
                status.setRollbackOnly();
                log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                throw new AppException(ResponseCode.INDEX_DUP.getCode());
            }finally {
                dbRouter.clear();
            }
        });
    }

    @Override
    public int subtractionActivitySkuStock(Long sku, Integer stockCountSurplus) {
        return raffleActivitySkuDao.subtractionActivitySkuStock(sku, stockCountSurplus);
    }
}
