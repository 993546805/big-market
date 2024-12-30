package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.DailyBehaviorRebate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tu
 * @date 2024-12-30 下午9:59
 */
@Mapper
public interface IDailyBehaviorRebateDao {
    List<DailyBehaviorRebate> queryDailyBehaviorRebateConfig(String behaviorType);
}
