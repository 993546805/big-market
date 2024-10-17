package com.tuto.domain.activity.service.rule.impl;

import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-10-17 15:43
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityRepository repository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        Integer stockCountSurplus = activitySkuEntity.getStockCountSurplus();
        if (stockCountSurplus <= 0) {
            return false;
        }

        int count = repository.subtractionActivitySkuStock(activitySkuEntity.getSku(), activitySkuEntity.getStockCountSurplus());
        if (count <= 0) {
            return false;
        }
        return true;
    }
}
