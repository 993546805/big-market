package com.tuto.domain.credit.repository;

import com.tuto.domain.credit.model.aggregate.TradeAggregate;

/**
 * 积分仓储层
 */
public interface ICreditRepository {

    /**
     * 保存积分交易订单
     * @param tradeAggregate 积分交易订单
     */
    void saveUserCreditTradeOrder(TradeAggregate tradeAggregate);
}
