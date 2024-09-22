package com.tuto.domain.strategy.service.rule.tree.factory.engine;


import com.tuto.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * 规则树组合接口
 */
public interface IDecisionTreeEngine {


    DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);
}
