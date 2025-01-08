package com.tuto.test.domain.credit;

import com.tuto.domain.credit.model.entity.TradeEntity;
import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import com.tuto.domain.credit.service.ICreditAdjustService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

/**
 * 积分调整服务测试类
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditAdjustServiceTest {

    @Resource
    private ICreditAdjustService creditAdjustService;

    /**
     * 测试创建支付订单 - 扣减
     */
    @Test
    public void test_createOrder_pay() throws InterruptedException {
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId("tuhb");
        tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setAmount(new BigDecimal("-1.68"));
        tradeEntity.setOutBusinessNo("7823974982374");
        creditAdjustService.createOrder(tradeEntity);
        new CountDownLatch(1).await();
    }
}