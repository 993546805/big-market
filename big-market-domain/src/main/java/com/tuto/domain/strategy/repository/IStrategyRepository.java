package com.tuto.domain.strategy.repository;

import com.tuto.domain.strategy.model.entity.StrategyAwardEntity;
import com.tuto.domain.strategy.model.entity.StrategyEntity;
import com.tuto.domain.strategy.model.entity.StrategyRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * 策略仓储
 */
public interface IStrategyRepository {
    /**
     * 查询策略奖品列表
     * @param strategyId 策略ID
     * @return 策略奖品列表
     */
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**
     * 存储策略奖品搜索命中概率表
     */
    void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable);

    /**
     * g
     * @param strategyId
     * @return
     */
    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String strategyId, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);
}
