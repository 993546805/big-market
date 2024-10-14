package com.tuto.domain.strategy.service;

import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * 抽奖策略接口
 * @author tu
 * @date 2024-09-10 22:05
 */
public interface IRaffleStock {


    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
