package com.tuto.domain.activity.service;

import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.domain.activity.service.rule.factory.DefaultActivityChainFactory;

/**
 * @author tu
 * @date 2024-10-17 15:22
 */
public class RaffleActivitySupport {

    protected DefaultActivityChainFactory defaultActivityChainFactory;

    protected IActivityRepository activityRepository;

    public RaffleActivitySupport(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }

    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }

    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        return activityRepository.queryRaffleActivityByActivityId(activityId);
    }

    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryRaffleActivityCountByActivityCountId(activityCountId);
    }
}
