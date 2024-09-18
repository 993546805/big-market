package com.tuto.test.domain;

import com.alibaba.fastjson2.JSON;
import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.service.IRaffleStrategy;
import com.tuto.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-09-11 22:19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(ruleWeightLogicFilter,"userScore",40500L);
    }


    /**
     * 用于测试:权重抽奖
     */
    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("tuhb")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("返回结果: {}", JSON.toJSONString(raffleAwardEntity));
    }

    /**
     * 用于测试:黑名单抽奖
     */
    @Test
    public void test_preformRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("返回结果: {}", JSON.toJSONString(raffleAwardEntity));
    }

    /**
     * 用于测试:白名单抽奖
     */
    @Test
    public void test_preformRaffle_whitelist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("tuhb")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数: {}", JSON.toJSONString(raffleFactorEntity));
        log.info("返回结果: {}", JSON.toJSONString(raffleAwardEntity));
    }
}
