package com.tuto.domain.activity.service.quota.policy.impl;

import com.tuto.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.tuto.domain.activity.model.valobj.OrderStateVO;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.stereotype.Service;

/**
 * 积分兑换,支付类订单
 *
 * @author tu
 * @date 2025-01-07 下午4:19
 */
@Service("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {

    private final IActivityRepository activityRepository;

    public CreditPayTradePolicy(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
