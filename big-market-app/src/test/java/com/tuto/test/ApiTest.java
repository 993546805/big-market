package com.tuto.test;

import com.alibaba.fastjson.JSON;
import com.tuto.infrastructure.persistent.dao.IStrategyDao;
import com.tuto.infrastructure.persistent.po.Strategy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IStrategyDao strategyDao;

    @Test
    public void test_orm() {
        Strategy strategy = strategyDao.queryStrategy(100001L);
        log.info("查询结果：{}", JSON.toJSONString(strategy));
    }



}
