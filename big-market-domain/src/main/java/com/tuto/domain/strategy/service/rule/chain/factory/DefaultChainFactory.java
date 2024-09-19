package com.tuto.domain.strategy.service.rule.chain.factory;

import com.tuto.domain.strategy.model.entity.StrategyEntity;
import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 工厂
 * @author tu
 * @date 2024-09-19 20:22
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;
    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.ruleModels();

        // 如果未配置策略规则，则只装填一个默认责任链
        if (null == ruleModels || 0 == ruleModels.length) return logicChainGroup.get("default");

        // 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight 「注意此数据从Redis缓存中获取，如果更新库表，记得在测试阶段手动处理缓存」
        ILogicChain rootChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain currentChain = rootChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain chain = logicChainGroup.get(ruleModels[i]);
            currentChain = currentChain.appendNext(chain);
        }

        // 责任链末端装配上默认责任链
        currentChain.appendNext(logicChainGroup.get("default"));

        return rootChain;
    }
}
