package com.tuto.domain.activity.service;

import com.tuto.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * sku 商品服务接口
 */
public interface IRaffleActivitySkuProductService {
    /**
     * 查询当前活动 ID 下,创建的 sku 商品. [sku 可以兑换活动抽奖次数]
     * @param activityId 活动 ID
     * @return 返回 sku 商品列表
     */
    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);
}
