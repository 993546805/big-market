package com.tuto.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.tuto.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import com.tuto.infrastructure.persistent.po.RaffleActivityOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author tu
 * @date 2024-10-17 09:45
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityOrderDaoTest {

    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    @Test
    public void test_insert() {
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setUserId("xiaofuge");
        raffleActivityOrder.setActivityId(100301L);
        raffleActivityOrder.setActivityName("测试活动");
        raffleActivityOrder.setStrategyId(100006L);
        raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setState("not_used");
        // 插入数据
        raffleActivityOrderDao.insert(raffleActivityOrder);
    }

    /**
     * 用于测试:不分库分表
     */
    @Test
    public void test() {
        List<RaffleActivityOrder> raffleActivityOrders = raffleActivityOrderDao.queryRaffleActivityOrderByUserId(1L);
        log.info("测试结果: {}", JSON.toJSONString(raffleActivityOrders));
    }
}
