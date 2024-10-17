package com.tuto.test.domain;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.activity.model.entity.SkuRechargeEntity;
import com.tuto.domain.activity.service.IRaffleOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
}
