package com.tuto.domain.strategy.service.raffle;

import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.model.entity.RuleActionEntity;
import com.tuto.domain.strategy.model.entity.RuleMatterEntity;
import com.tuto.domain.strategy.model.entity.StrategyAwardEntity;
import com.tuto.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.tuto.domain.strategy.model.valobj.RuleTreeVO;
import com.tuto.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.tuto.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.AbstractRaffleStrategy;
import com.tuto.domain.strategy.service.IRaffleAward;
import com.tuto.domain.strategy.service.IRaffleRule;
import com.tuto.domain.strategy.service.IRaffleStock;
import com.tuto.domain.strategy.service.armory.IStrategyDispatch;
import com.tuto.domain.strategy.service.rule.chain.ILogicChain;
import com.tuto.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.tuto.domain.strategy.service.rule.filter.ILogicFilter;
import com.tuto.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.tuto.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.tuto.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tu
 * @date 2024-09-11 21:44
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward, IRaffleRule {

    @Resource
    private DefaultLogicFactory logicFactory;

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory,defaultTreeFactory);
    }

    @Override
    protected DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key,未在库表 rule_tree rule_tree_node rule_tree_line 配置对应的规则信息 " + strategyAwardRuleModelVO.getRuleModels());
        }

        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    protected DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain chain = defaultChainFactory.openLogicChain(strategyId);
        return chain.logic(userId, strategyId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public void clearQueueValue() {
        repository.clearQueueValue();
    }

    @Override
    public void clearStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.clearStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivity(activityId);
        return queryRaffleStrategyAwardList(strategyId);
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        return repository.queryAwardRuleLockCount(treeIds);
    }
}
