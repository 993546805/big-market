package com.tuto.infrastructure.persistent.repository;

import com.tuto.domain.activity.event.AwardStockZeroMessageEvent;
import com.tuto.domain.strategy.model.entity.StrategyAwardEntity;
import com.tuto.domain.strategy.model.entity.StrategyEntity;
import com.tuto.domain.strategy.model.entity.StrategyRuleEntity;
import com.tuto.domain.strategy.model.valobj.*;
import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.tuto.infrastructure.event.EventPublisher;
import com.tuto.infrastructure.persistent.dao.*;
import com.tuto.infrastructure.persistent.po.*;
import com.tuto.infrastructure.persistent.redis.IRedisService;
import com.tuto.types.common.Constants;
import com.tuto.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.tuto.types.enums.ResponseCode.UN_ASSEMBLED_STRATEGY_ARMORY;

/**
 * @author tu
 * @date 2024-09-09 10:53
 */
@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IRaffleActivityDao activityDao;
    @Resource
    private IStrategyAwardDao strategyAwardDao;
    @Resource
    private IStrategyRuleDao strategyRuleDao;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IRedisService redisService;
    @Resource
    private IRuleTreeDao ruleTreeDao;
    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private AwardStockZeroMessageEvent awardStockZeroMessageEvent;
    @Autowired
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;
    @Autowired
    private IRaffleActivityDao iRaffleActivityDao;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 优先从缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> cache = redisService.getValue(cacheKey);
        if (!CollectionUtils.isEmpty(cache)) {
            return cache;
        }

        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList(strategyId);
        if (CollectionUtils.isEmpty(strategyAwards)) {
            return Collections.emptyList();
        }
        List<StrategyAwardEntity> list = new ArrayList<>();
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            strategyAwardEntity.setStrategyId(strategyAward.getStrategyId());
            strategyAwardEntity.setAwardId(strategyAward.getAwardId());
            strategyAwardEntity.setAwardTitle(strategyAward.getAwardTitle());
            strategyAwardEntity.setAwardSubtitle(strategyAward.getAwardSubtitle());
            strategyAwardEntity.setAwardCount(strategyAward.getAwardCount());
            strategyAwardEntity.setAwardCountSurplus(strategyAward.getAwardCountSurplus());
            strategyAwardEntity.setAwardRate(strategyAward.getAwardRate());
            strategyAwardEntity.setSort(strategyAward.getSort());
            strategyAwardEntity.setRuleModels(strategyAward.getRuleModels());
            list.add(strategyAwardEntity);
        }
        // 缓存到Redis
        redisService.setValue(cacheKey, list);

        return list;
    }

    @Override
    public StrategyAwardEntity queryStrategyAward(Long strategyId, Integer awardId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
        if (null != strategyAwardEntity) return strategyAwardEntity;
        // 查询数据
        StrategyAward strategyAwardReq = new StrategyAward();
        strategyAwardReq.setStrategyId(strategyId);
        strategyAwardReq.setAwardId(awardId);
        StrategyAward strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAwardReq);
        // 转换数据
        strategyAwardEntity = StrategyAwardEntity.builder()
                .strategyId(strategyAwardRes.getStrategyId())
                .awardId(strategyAwardRes.getAwardId())
                .awardTitle(strategyAwardRes.getAwardTitle())
                .awardSubtitle(strategyAwardRes.getAwardSubtitle())
                .awardCount(strategyAwardRes.getAwardCount())
                .awardCountSurplus(strategyAwardRes.getAwardCountSurplus())
                .awardRate(strategyAwardRes.getAwardRate())
                .sort(strategyAwardRes.getSort())
                .build();
        // 缓存结果
        redisService.setValue(cacheKey, strategyAwardEntity);
        // 返回数据
        return strategyAwardEntity;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Integer> strategyAwardSearchRateTable) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        String cacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;
        if (!redisService.isExists(cacheKey)) {
            throw new AppException(UN_ASSEMBLED_STRATEGY_ARMORY.getCode(), cacheKey + Constants.COLON + UN_ASSEMBLED_STRATEGY_ARMORY.getInfo());
        }
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public Integer getStrategyAwardAssemble(String strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 1.从缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (null != strategyEntity) {
            return strategyEntity;
        }

        // 2.从数据库获取
        Strategy strategy = strategyDao.queryStrategy(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();
        // 3.缓存
        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);

        StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        return StrategyRuleEntity.builder()
                .awardId(strategyRule.getAwardId())
                .ruleDesc(strategyRule.getRuleDesc())
                .ruleType(strategyRule.getRuleType())
                .ruleValue(strategyRule.getRuleValue())
                .ruleModel(strategyRule.getRuleModel())
                .strategyId(strategyRule.getStrategyId())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return this.queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule = new StrategyRule();
        strategyRule.setStrategyId(strategyId);
        strategyRule.setAwardId(awardId);
        strategyRule.setRuleModel(ruleModel);
        return strategyRuleDao.queryStrategyRuleValue(strategyRule);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward req = new StrategyAward();
        req.setStrategyId(strategyId);
        req.setAwardId(awardId);
        String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(req);
        if (null == ruleModels) {
            return null;
        }
        return StrategyAwardRuleModelVO.builder()
                .ruleModels(ruleModels)
                .build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (null != ruleTreeVOCache) {
            return ruleTreeVOCache;
        }

        // 从数据库获取
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

        // 1. tree node line 转 map 结构
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeListMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeListMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }

        // 2. tree node 转为 map 结构
        Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeListMap.get(ruleTreeNode.getRuleKey()))
                    .build();

            treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }

        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();

        redisService.setValue(cacheKey, ruleTreeVODB);
        return ruleTreeVODB;
    }

    @Override
    public void cacheStrategyAwardCount(String cacheKey, Integer awardCount) {
        if (redisService.isExists(cacheKey)) return;
        redisService.setAtomicLong(cacheKey, awardCount);
    }

    @Override
    public Boolean subtractionAwardStock(String cacheKey) {
        return subtractionAwardStock(cacheKey, null);
    }

    @Override
    public Boolean subtractionAwardStock(String cacheKey, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus < 0) {
            // 库存小于 0 恢复 0
            redisService.setAtomicLong(cacheKey, 0);
            return false;
        }
        // 1. 按照 cacheKey decr 后的值,如 99 98 97 和 key 组成为库存锁进行使用.
        // 2. 加锁为了兜底,如果后续有恢复库存,手动处理等,也不会超卖.因为多有的可用库存 key,都被加锁了
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = false;
        if (null != endDateTime) {
            long expireMills = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
            lock = redisService.setNx(lockKey, expireMills, TimeUnit.MICROSECONDS);
        } else {
            lock = redisService.setNx(lockKey);
        }
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }


    public Boolean subtractionAwardStock(String cacheKey, Long strategyId, Integer awardId, Date endDateTime) {
        long surplus = redisService.decr(cacheKey);
        if (surplus == 0) {
            // 库存消耗没了以后，发送MQ消息，更新数据库库存
            eventPublisher.publish(awardStockZeroMessageEvent.getTopic(), awardStockZeroMessageEvent.buildEventMessage(AwardStockZeroMessageEvent.AwardStockZeroVo.builder().strategyId(strategyId).awardId(awardId).build()));
        } else if (surplus < 0) {
            // 重置库存为0
            redisService.setValue(cacheKey, 0);
            return false;
        }
        String lockKey = cacheKey + Constants.UNDERLINE + surplus;
        Boolean lock = redisService.setNx(lockKey);
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(strategyAwardStockKeyVO, 3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public void clearQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUERY_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> destinationQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redisService.getDelayedQueue(destinationQueue);
        delayedQueue.clear();
        destinationQueue.clear();
    }

    @Override
    public void clearStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyAwardDao.clearStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public Long queryStrategyIdByActivity(Long activityId) {
        return activityDao.queryStrategyIdByActivityId(activityId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if (null == treeIds || treeIds.length == 0) return new HashMap<>();
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleLocks(treeIds);
        Map<String, Integer> resultMap = new HashMap<>();
        for (RuleTreeNode node : ruleTreeNodes) {
            String treeId = node.getTreeId();
            Integer ruleCount = Integer.valueOf(node.getRuleValue());
            resultMap.put(treeId, ruleCount);
        }
        return resultMap;
    }

    @Override
    public Integer queryTodayUserRaffleCount(String userId, Long strategyId) {
        Long activityId = iRaffleActivityDao.queryActivityIdByStrategyId(strategyId);
        // 封装参数
        RaffleActivityAccountDay raffleActivityAccountDayReq = new RaffleActivityAccountDay();
        raffleActivityAccountDayReq.setUserId(userId);
        raffleActivityAccountDayReq.setActivityId(activityId);
        raffleActivityAccountDayReq.setDay(raffleActivityAccountDayReq.currentDay());
        RaffleActivityAccountDay raffleActivityAccountDay = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDayReq);
        if (null == raffleActivityAccountDay) {
            return 0;
        }
        return raffleActivityAccountDay.getDayCount() - raffleActivityAccountDay.getDayCountSurplus();
    }

    @Override
    public List<RuleWeightVO> queryAwardRuleWeight(Long strategyId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        List<RuleWeightVO> ruleWeightVOS = redisService.getValue(cacheKey);
        if (null != ruleWeightVOS) {
            return ruleWeightVOS;
        }

        ruleWeightVOS = new ArrayList<>();
        // 1.查询权重规则配置
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        String ruleValue = strategyRuleDao.queryStrategyRuleValue(strategyRuleReq);
        // 2.借助实体对象转换规则
        StrategyRuleEntity strategyRuleEntity = new StrategyRuleEntity();
        strategyRuleEntity.setRuleModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode());
        strategyRuleEntity.setRuleValue(ruleValue);
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        // 3. 遍历规则组装奖品数据
        Set<String> ruleWeightKeySet = ruleWeightValues.keySet();
        for (String ruleWeightKey : ruleWeightKeySet) {
            List<Integer> awardIds = ruleWeightValues.get(ruleWeightKey);
            List<RuleWeightVO.Award> awardList = new ArrayList<>();
            for (Integer awardId : awardIds) {
                StrategyAward strategyAwardReq = new StrategyAward();
                strategyAwardReq.setStrategyId(strategyId);
                strategyAwardReq.setAwardId(awardId);
                StrategyAward strategyAward = strategyAwardDao.queryStrategyAward(strategyAwardReq);
                awardList.add(RuleWeightVO.Award.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .build());
            }
            ruleWeightVOS.add(RuleWeightVO.builder()
                            .awardIds(awardIds)
                            .ruleValue(ruleValue)
                            .weight(Integer.valueOf(ruleWeightKey.split(Constants.COLON)[0]))
                            .awardList(awardList)
                    .build());
        }

        // 设置缓存 - 实际场景中,这类数据,可以在活动下架的时候同意清空缓存
        redisService.setValue(cacheKey, ruleWeightVOS);
        return ruleWeightVOS;
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        strategyAwardDao.updateStrategyAwardStock(strategyAward);
    }


}
