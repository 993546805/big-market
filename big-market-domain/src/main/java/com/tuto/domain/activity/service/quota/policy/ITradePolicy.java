package com.tuto.domain.activity.service.quota.policy;

import com.tuto.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

/**
 * 交易策略
 */
public interface ITradePolicy {

    /**
     * 交易
     * @param createQuotaOrderAggregate 创建额度订单聚合对象
     */
    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
