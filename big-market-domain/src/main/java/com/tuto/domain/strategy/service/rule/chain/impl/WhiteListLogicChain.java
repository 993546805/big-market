package com.tuto.domain.strategy.service.rule.chain.impl;

import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.rule.chain.AbstractILogicChain;
import com.tuto.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-09-19 20:52
 */
@Slf4j
@Component("rule_whitelist")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WhiteListLogicChain extends AbstractILogicChain implements Cloneable{

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-白名单开始 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, ruleModel());

        String[] split = ruleValue.split(Constants.COLON);

        Integer awardId = Integer.parseInt(split[0]);

        String[] userWhiteIds = split[1].split(Constants.SPLIT);
        for (String userWhiteId : userWhiteIds) {
            if (userId.equals(userWhiteId)) {
                log.info("抽奖责任链-白名单命中 userId:{} strategyId:{} ruleModel:{} awardId:{}", userId, strategyId, ruleModel(), awardId);
                return awardId;
            }
        }
        log.info("抽奖责任链-白名单未命中 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }


    @Override
    protected String ruleModel() {
        return "rule_whitelist";
    }

}
