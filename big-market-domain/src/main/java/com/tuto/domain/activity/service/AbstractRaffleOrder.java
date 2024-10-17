package com.tuto.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.activity.model.entity.*;
import com.tuto.domain.activity.repository.IActivityRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-10-17 11:14
 */
@Slf4j
public abstract class AbstractRaffleOrder implements IRaffleOrder {

    protected final IActivityRepository activityRepository;

    public AbstractRaffleOrder(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1.通过 sku 查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2.查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3.查询次数信息
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));
        return ActivityOrderEntity.builder().build();
    }
}
