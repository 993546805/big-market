package com.tuto.test.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.tuto.trigger.api.IRaffleActivityService;
import com.tuto.trigger.api.dto.ActivityDrawRequestDTO;
import com.tuto.trigger.api.dto.ActivityDrawResponseDTO;
import com.tuto.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
    public void test_draw() {
        ActivityDrawRequestDTO request = new ActivityDrawRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("xiaofuge");
        Response<ActivityDrawResponseDTO> response = raffleActivityService.draw(request);

        log.info("请求参数：{}", JSON.toJSONString(request));
        log.info("测试结果：{}", JSON.toJSONString(response));
    }

    /**
     * 用于测试: 查询是否签到
     */
    @Test
    public void test_isCalendarSignRebate() {
        Response<Boolean> isCalendarSignRebate = raffleActivityService.isCalendarSignRebate("xiaofuge");
        log.info("测试结果：{}", JSON.toJSONString(isCalendarSignRebate));
    }

    /**
     * 用于测试: 签到
     */
    @Test
    public void test_calendarSignRebate() {
        Response<Boolean> calendarSignRebate = raffleActivityService.calendarSignRebate("xiaofuge");
        log.info("测试结果：{}", JSON.toJSONString(calendarSignRebate));
    }
}

