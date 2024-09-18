package com.tuto.domain.strategy.service.rule.factory;

import com.tuto.domain.strategy.model.entity.RuleActionEntity;
import com.tuto.domain.strategy.service.annotation.LogicStrategy;
import com.tuto.domain.strategy.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则工厂
 *
 * @author tu
 * @date 2024-09-10 22:14
 */
@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String,ILogicFilter<T>> openLogicFilter() {
        return (Map<String,ILogicFilter<T>>) (Map<?,?>)logicFilterMap;
    }


    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_WEIGHT("rule_weight", "[抽奖前规则]根据抽奖权重返回可抽奖范围"),
        RULE_BLACKLIST("rule_blacklist", "[抽奖前规则]黑名单规则过滤,命中黑名单则直接返回"),
        RULE_WHITE_LIST("rule_whitelist", "[抽奖前规则]白名单规则过滤,命中白名单则直接返回"),
        ;

        private final String code;
        private final String info;
    }
}
