package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 策略规则
 */
@Mapper
public interface IStrategyRuleDao {

    StrategyRule queryStrategyRule(StrategyRule req);

    String queryStrategyRuleValue(StrategyRule strategyRule);
}
