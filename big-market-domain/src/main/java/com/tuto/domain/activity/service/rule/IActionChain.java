package com.tuto.domain.activity.service.rule;

import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author tu
 * @date 2024-10-17 15:39
 */
public interface IActionChain extends IActionChainArmory{

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
