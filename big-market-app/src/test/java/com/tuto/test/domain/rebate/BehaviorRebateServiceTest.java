package com.tuto.test.domain.rebate;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.rebate.IBehaviorRebateService;
import com.tuto.domain.rebate.model.entity.BehaviorEntity;
import com.tuto.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tu
 * @date 2024-12-30 下午10:24
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BehaviorRebateServiceTest {

    @Resource
    private IBehaviorRebateService behaviorRebateService;


    @Test
    public void testCreateOrder() {
        BehaviorEntity entity = BehaviorEntity.builder()
                .userId("tuhb")
                .behaviorTypeVO(BehaviorTypeVO.SING)
                .outBusinessNo("123")
                .build();

        List<String> order = behaviorRebateService.createOrder(entity);
        log.info("请求参数: {}", JSON.toJSONString(entity));
        log.info("测试结果: {}", JSON.toJSONString(order));
    }
}
