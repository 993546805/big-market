package com.tuto.test.domain;

import com.alibaba.fastjson2.JSON;
import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.service.IRaffleStrategy;
import com.tuto.domain.strategy.service.armory.IStrategyArmory;
import com.tuto.domain.strategy.service.rule.chain.impl.WeightLogicChain;
import com.tuto.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
import com.tuto.domain.strategy.service.rule.filter.impl.RuleWeightLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

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
    private WeightLogicChain weightLogicChain;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Before
    public void setUp() {
//        ReflectionTestUtils.setField(weightLogicChain,"userScore",40500L);
//        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100001L));
        log.info("测试结果：{}", strategyArmory.assembleLotteryStrategy(100006L));
//        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 7L);
    }


    @Test
    public void test_performRaffle() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("xiaofuge")
                    .strategyId(100006L)
                    .build();
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
        // 等待 UpdateAwardStockJob 消费队列
        new CountDownLatch(1).await();
    }


}
