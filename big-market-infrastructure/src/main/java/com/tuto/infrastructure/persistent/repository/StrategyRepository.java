package com.tuto.infrastructure.persistent.repository;

import com.tuto.domain.strategy.model.entity.StrategyAwardEntity;
import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.infrastructure.persistent.dao.IStrategyAwardDao;
import com.tuto.infrastructure.persistent.po.StrategyAward;
import com.tuto.infrastructure.persistent.redis.IRedisService;
import com.tuto.types.common.Constants;
import jodd.util.CollectionUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tu
 * @date 2024-09-09 10:53
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList(strategyId);
        if (CollectionUtils.isEmpty(strategyAwards)) {
            return Collections.emptyList();
        }
        List<StrategyAwardEntity> list = new ArrayList<>();
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            strategyAwardEntity.setStrategyId(strategyAward.getStrategyId());
            strategyAwardEntity.setAwardId(strategyAward.getAwardId());
            strategyAwardEntity.setAwardCount(strategyAward.getAwardCount());
            strategyAwardEntity.setAwardCountSurplus(strategyAward.getAwardCountSurplus());
            strategyAwardEntity.setAwardRate(strategyAward.getAwardRate());

            list.add(strategyAwardEntity);
        }
        return list;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(Long strategyId, int rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public Integer getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }
}
