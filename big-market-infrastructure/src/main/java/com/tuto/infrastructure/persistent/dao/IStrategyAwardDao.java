package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 策略奖品配置
 * @author tu
 * @date 2024-09-06 15:19
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList(Long strategyId);
}
