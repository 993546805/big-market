package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.Strategy;
import com.tuto.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 策略持久层
 * @author tu
 * @date 2024-09-06 15:18
 */
@Mapper
public interface IStrategyDao {

    Strategy queryStrategy(Long strategyId);

}
