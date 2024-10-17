package com.tuto.domain.activity.repository;

import com.tuto.domain.activity.model.aggregate.CreateOrderAggregate;
import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;

public interface IActivityRepository {

    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    int subtractionActivitySkuStock(Long sku, Integer stockCountSurplus);
}
