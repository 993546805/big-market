package com.tuto.test.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.tuto.trigger.api.IRaffleActivityService;
import com.tuto.trigger.api.IRaffleStrategyService;
import com.tuto.trigger.api.dto.ActivityDrawRequestDTO;
import com.tuto.trigger.api.dto.ActivityDrawResponseDTO;
import com.tuto.trigger.api.dto.RaffleAwardListRequestDTO;
import com.tuto.trigger.api.dto.RaffleAwardListResponseDTO;
import com.tuto.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 抽奖活动服务测试
 * @author tu
 * @date 2024-12-25 下午6:10
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyControllerTest {

    @Resource
    private IRaffleStrategyService raffleStrategyService;



    /**
     * 用于测试: 获取奖品列表
     */
    @Test
    public void test_awardList() {
        RaffleAwardListRequestDTO request = new RaffleAwardListRequestDTO();
        request.setActivityId(100301L);
        request.setUserId("xiaofuge");
        Response<List<RaffleAwardListResponseDTO>> response = raffleStrategyService.queryRaffleAwardList(request);
        log.info("测试结果: {}",JSON.toJSONString(response));
    }



}
