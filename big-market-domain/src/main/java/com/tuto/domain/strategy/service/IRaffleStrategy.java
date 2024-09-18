package com.tuto.domain.strategy.service;

import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * 抽奖策略接口
 * @author tu
 * @date 2024-09-10 22:05
 */
public interface IRaffleStrategy {

    /**
     * 执行抽奖; 用于抽奖因子入参,执行抽奖计算,返回奖品信息
     * @param raffleFactorEntity 抽奖因子实体对象,根据入参信息计算抽奖结果.
     * @return 抽奖的奖品
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
