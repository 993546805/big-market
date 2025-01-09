package com.tuto.test.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.tuto.trigger.api.IRaffleActivityService;
import com.tuto.trigger.api.dto.ActivityDrawRequestDTO;
import com.tuto.trigger.api.dto.ActivityDrawResponseDTO;
import com.tuto.trigger.api.dto.CreditPayExchangeSkuRequestDTO;
import com.tuto.trigger.api.dto.SkuProductResponseDTO;
import com.tuto.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author tu
 * @date 2024-12-25 下午6:07
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityControllerTest {

    @Resource
    private IRaffleActivityService raffleActivityService;


    @Test
    public void test_armory(){
        Response<Boolean> response = raffleActivityService.armory(100301L);
        log.info("测试结果: {}", JSON.toJSONString(response));
    }

    /**
     * 用于测试:
     */
    @Test
    public void test_draw() throws InterruptedException {
        ActivityDrawRequestDTO request = new ActivityDrawRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("tuhb");
        Response<ActivityDrawResponseDTO> response = raffleActivityService.draw(request);

        log.info("请求参数：{}", JSON.toJSONString(request));
        log.info("测试结果：{}", JSON.toJSONString(response));
        new CountDownLatch(1).await();
    }

    /**
     * 用于测试: 查询是否签到
     */
    @Test
    public void test_isCalendarSignRebate() {
        Response<Boolean> isCalendarSignRebate = raffleActivityService.isCalendarSignRebate("tuhb");
        log.info("测试结果：{}", JSON.toJSONString(isCalendarSignRebate));
    }

    /**
     * 用于测试: 签到
     */
    @Test
    public void test_calendarSignRebate() throws InterruptedException {
        Response<Boolean> calendarSignRebate = raffleActivityService.calendarSignRebate("tuhb");
        log.info("测试结果：{}", JSON.toJSONString(calendarSignRebate));
        new CountDownLatch(1).await();
    }


    /**
     * 用于测试: 活动下的 sku 商品列表查询
     */
    @Test
    public void test_queryActivitySkuProductList() {
        Response<List<SkuProductResponseDTO>> listResponse = raffleActivityService.querySkuProductListByActivityId(100301L);
        log.info("测试结果：{}", JSON.toJSONString(listResponse));
    }
    
    /**
     * 用于测试: 可用积分
     */
    @Test
    public void test_userCredit() {
        Response<BigDecimal> userCredit = raffleActivityService.queryUserCredit("tuhb");
        log.info("测试结果：{}", JSON.toJSONString(userCredit));
    }

    /**
     * 用于测试: 积分购买 sku
     */
    @Test
    public void test_creditExchangeSkuProduct() throws InterruptedException {
        CreditPayExchangeSkuRequestDTO request = new CreditPayExchangeSkuRequestDTO();
        request.setSku(9011L);
        request.setUserId("tuhb");
        Response<Boolean> response = raffleActivityService.creditPayExchangeSku(request);
        log.info("测试结果：{}", JSON.toJSONString(response));
    }
}

