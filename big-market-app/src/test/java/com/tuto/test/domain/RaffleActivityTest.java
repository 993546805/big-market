package com.tuto.test.domain;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.activity.model.entity.SkuRechargeEntity;
import com.tuto.domain.activity.service.IRaffleOrder;
import com.tuto.domain.activity.service.armory.IActivityArmory;
import com.tuto.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author tu
 * @date 2024-10-17 14:34
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityTest {

    @Resource
    private IRaffleOrder raffleOrder;
    @Resource
    private IActivityArmory activityArmory;

    /**
     * 用于测试: 创建抽奖单
     */
    @Test
    public void test() {
        SkuRechargeEntity activityShopCartEntity = SkuRechargeEntity.builder()
                .sku(9011L)
                .userId("tuhb")
                .outBusinessNo("sdlkfjo0011")
                .build();
        String orderId = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果：{}", JSON.toJSONString(orderId));
    }


    @Before
    public void setUp(){
        log.info("装配活动: {}",activityArmory.assembleActivitySku(9011L));
    }

    /**
     * 用于测试:库存消耗和最终一致性更新
     * 1. raffle_activity_sku 库表可以设置 20 个
     * 2. 清空 redis 缓存 flushall
     * 3. for 循环 20 次,消耗完库存,最终数据库剩余库存为 0
     */
    @Test
    public void test_createSkuRechargeOrder() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            try {
                SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
                skuRechargeEntity.setSku(9011L);
                skuRechargeEntity.setUserId("tuhb");
                skuRechargeEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
                String orderId = raffleOrder.createRaffleActivityOrder(skuRechargeEntity);
                log.info("测试结果：{}",orderId);
            } catch (AppException e) {
                log.warn(e.getInfo());
            }
        }

        new CountDownLatch(1).await();
    }
}
