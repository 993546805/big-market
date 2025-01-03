package com.tuto.domain.rebate;

import com.tuto.domain.rebate.model.entity.BehaviorEntity;
import com.tuto.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * 行为返利服务接口
 * @author tu
 * @date 2024-12-26 下午5:35
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity entity);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo);
}
