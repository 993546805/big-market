package com.tuto.domain.activity.service.product;

import com.tuto.domain.activity.model.entity.SkuProductEntity;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.domain.activity.service.IRaffleActivitySkuProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author tu
 * @date 2025-01-08 下午6:16
 */
@Service
public class RaffleActivitySkuProductService implements IRaffleActivitySkuProductService {
    @Resource
    private IActivityRepository activityRepository;
    @Override
    public List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId) {
        return activityRepository.querySkuProductEntityListByActivityId(activityId);
    }
}
