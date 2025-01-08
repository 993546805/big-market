package com.tuto.domain.activity.service.quota.policy.impl;

import com.tuto.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.tuto.domain.activity.model.valobj.OrderStateVO;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 返利无支付交易订单,直接充值到账
 *
 * @author tu
 * @date 2025-01-07 下午4:21
 */
@Service("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public RebateNoPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        // 不需要支付则修改订单金额为 0,状态为完成,直接给用户账户充值
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveNoPayOrder(createQuotaOrderAggregate);
    }
}
