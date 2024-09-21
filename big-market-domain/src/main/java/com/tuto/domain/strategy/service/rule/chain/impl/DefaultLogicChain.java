package com.tuto.domain.strategy.service.rule.chain.impl;

import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.armory.IStrategyDispatch;
import com.tuto.domain.strategy.service.rule.chain.AbstractILogicChain;
import com.tuto.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
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
@Component("default")
public class DefaultLogicChain extends AbstractILogicChain {

    @Resource
    private IStrategyRepository strategyRepository;
    @Resource
    private IStrategyDispatch strategyDispatch;

    // 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
    public Long userScore = 0L;

    @Override
    public Integer logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链-默认处理 userId:{} strategyId:{} ruleModel:{} awardId:{}", userId, strategyId, ruleModel(),awardId);

        return awardId;
    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
