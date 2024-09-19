package com.tuto.domain.strategy.service.rule.chain.impl;

import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.armory.IStrategyDispatch;
import com.tuto.domain.strategy.service.rule.chain.AbstractILogicChain;
import com.tuto.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tu
 * @date 2024-09-19 20:33
 */
@Slf4j
@Component("rule_weight")
public class WeightLogicChain extends AbstractILogicChain {

    @Resource
    private IStrategyRepository strategyRepository;
    @Resource
    private IStrategyDispatch strategyDispatch;

    // 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
    public Long userScore = 0L;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());

        // 查询规则配置
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, ruleModel());

        // 处理规则值
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) return null;

        // 找出最大的小于 key符合规则的值
        ArrayList<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        // 倒序
        Collections.sort(analyticalSortedKeys, (o1, o2) -> o2.compareTo(o1));
        Long nextValue = analyticalSortedKeys.stream()
                .filter(val -> userScore > val)
                .findFirst()
                .orElse(null);

        // 权重抽奖判断
        if (null != nextValue) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重命中 userId:{} strategyId:{} ruleModel:{} awardId:{}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }

        // 过滤其他责任链
        log.info("抽奖责任链-权重未命中 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());

        return next().logic(userId, strategyId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);

        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }


    @Override
    protected String ruleModel() {
        return "rule_weight";
    }
}
