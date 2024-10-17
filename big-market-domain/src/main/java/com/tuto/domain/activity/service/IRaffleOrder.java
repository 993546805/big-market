package com.tuto.domain.activity.service;


import com.tuto.domain.activity.model.entity.ActivityOrderEntity;
import com.tuto.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * 抽奖活动订单接口
 */
public interface IRaffleOrder {

    /**
     * 以 sku创建抽奖活动订单, 获得参与抽奖资格(可消耗的次数)
     * @param activityShopCartEntity 活动 sku 实体,通过sku领取活动
     * @return 活动参与记录实体
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
