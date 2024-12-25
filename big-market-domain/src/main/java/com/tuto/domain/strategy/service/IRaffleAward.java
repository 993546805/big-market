package com.tuto.domain.strategy.service;

import com.tuto.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author tu
 * @date 2024-10-14 15:38
 */
public interface IRaffleAward  {

    /**
     * 根据策略 ID 查询抽奖奖品列表配置
     * @param strategyId 策略 ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);


    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId);
}
