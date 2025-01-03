package com.tuto.test.domain.award;

import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import com.tuto.domain.award.model.valobj.AwardStateVO;
import com.tuto.domain.award.service.IAwardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author tu
 * @date 2024-12-24 下午7:48
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardServiceTest {

    @Resource
    private IAwardService awardService;


    /**
     * 用于测试: 模拟发放抽奖记录,流程中会发送 mq,以及接收 mq 消息,还有 task 表,补偿发送 mq
     */
    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            UserAwardRecordEntity userAwardRecordEntity = new UserAwardRecordEntity();
            userAwardRecordEntity.setUserId("tuhb");
            userAwardRecordEntity.setActivityId(100301L);
            userAwardRecordEntity.setStrategyId(100006L);
            userAwardRecordEntity.setOrderId(RandomStringUtils.randomNumeric(12));
            userAwardRecordEntity.setAwardId(101);
            userAwardRecordEntity.setAwardTitle("OpenAI 增加使用次数");
            userAwardRecordEntity.setAwardTime(new Date());
            userAwardRecordEntity.setAwardState(AwardStateVO.create);

            awardService.saveUserAwardRecord(userAwardRecordEntity);
            Thread.sleep(500L);
        }

        new CountDownLatch(1).await();
    }
}
