package com.tuto.domain.strategy.service.armory;

import java.util.Date;

/**
 * 策略抽奖调度
 * @author tu
 * @date 2024-09-09 15:52
 */
public interface IStrategyDispatch {
    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @param ruleWeightValue 规则权重值
     * @return 抽奖结果
     */
    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 扣减库存
     * @param strategyId 策略ID
     * @param awardId 奖品 ID
     * @param endDateTime 结束时间
     * @return 是否扣减成功
     */
    Boolean subtractionAwardStock(Long strategyId, Integer awardId, Date endDateTime);
}
