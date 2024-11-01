package com.tuto.domain.activity.service.armory;


import java.util.Date;

/**
 * 活动调度[扣减库存]
 */
public interface IActivityDispatch {

    /**
     * 根据策略 ID 和奖品 ID,扣减奖品库存
     * @param sku 互动 SKU
     * @param endDateTime 活动结束时间,根据结束时间设置加锁的 key 为结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
