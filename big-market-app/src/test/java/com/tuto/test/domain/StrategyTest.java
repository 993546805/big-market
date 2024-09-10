package com.tuto.test.domain;

import com.tuto.domain.strategy.service.IStrategyArmory;
import com.tuto.domain.strategy.service.IStrategyDispatch;
import com.tuto.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

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
    @Resource
    private IStrategyDispatch strategyDispatch;


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
        Integer randomAwardId = strategyDispatch.getRandomAwardId(100001L);
        log.info("测试结果: {}",randomAwardId);
    }

    @Resource
    private IRedisService redisService;

    /**
     * 用于测试:redis
     */
    @Test
    public void test_redis() {
        Map<Integer, Integer> map = redisService.getMap("big_market_strategy_rate_table_key_100001_6000:102,103,104,105,106,107,108,109");
        log.info("测试结果: {}",map.size());
    }

    /**
     * 用于测试:根据某个规则抽奖
     */
    @Test
    public void test_getAssembleRandomVal2() {
        log.info("测试结果：{} - 6000 策略配置", strategyDispatch.getRandomAwardId(100001L, "6000:102,103,104,105,106,107,108,109"));
    }
}
