package com.tuto.test.domain;

import com.tuto.domain.strategy.service.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 策略领域测试
 * @author tu
 * @date 2024-09-09 13:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;


    /**
     * 用于测试:某个策略装配到Redis Map
     */
    @Test
    public void test_strategyArmory() {
        boolean success = strategyArmory.assembleLotteryStrategy(100001L);
        log.info("测试结果: {}",success);
    }

    /**
     * 用于测试: 获取某个策略下的随机奖励id
     */
    @Test
    public void test_getAssembleRandomVal() {
        Integer randomAwardId = strategyArmory.getRandomAwardId(100001L);
        log.info("测试结果: {}",randomAwardId);
    }
}
