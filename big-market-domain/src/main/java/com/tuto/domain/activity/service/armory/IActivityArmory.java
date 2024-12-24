package com.tuto.domain.activity.service.armory;

/**
 * 活动装配预热
 * @author tu
 * @date 2024-10-30 21:20
 */
public interface IActivityArmory {

    boolean assembleActivitySku(Long sku);

    boolean assembleActivitySkuByActivityId(Long activityId);
}
