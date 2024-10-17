package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.RaffleActivityCount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抽奖活动次数配置表Dao
 * @author tu
 * @date 2024-10-17 11:23
 */
@Mapper
public interface IRaffleActivityCountDao {
    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
