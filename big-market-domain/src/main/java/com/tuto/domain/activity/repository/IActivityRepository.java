package com.tuto.domain.activity.repository;

import com.tuto.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.tuto.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.tuto.domain.activity.model.entity.*;
import com.tuto.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);


    boolean subtractionActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO activitySkuStockKeyVO);

    ActivitySkuStockKeyVO takeQueueValue(String sku);

    void clearQueueValue(String sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    /**
     * 查询未使用的抽奖订单
     *
     * @param userId     用户ID
     * @param activityId 活动ID
     * @return
     */
    UserRaffleOrderEntity queryNoUsedRaffleOrder(String userId, Long activityId);

    /**
     * 保存创建抽奖订单聚合对象
     *
     * @param createPartakeOrderAggregate
     */
    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);

    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);

    List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId);

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);

    Set<Long> queryActivitySkuList();

    ActivityAccountEntity queryActivityAccountEntity(Long activityId, String userId);

    void doSaveNoPayOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void doSaveCreditPayOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void updateOrder(DeliveryOrderEntity deliveryOrderEntity);
}
