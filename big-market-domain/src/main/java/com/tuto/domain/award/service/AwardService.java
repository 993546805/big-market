package com.tuto.domain.award.service;

import com.tuto.domain.award.event.SendAwardMessageEvent;
import com.tuto.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.tuto.domain.award.model.entity.DistributeAwardEntity;
import com.tuto.domain.award.model.entity.TaskEntity;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import com.tuto.domain.award.model.valobj.TaskStateVO;
import com.tuto.domain.award.repository.IAwardRepository;
import com.tuto.domain.award.service.distribute.IDistributeAward;
import com.tuto.types.event.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author tu
 * @date 2024-12-24 下午3:11
 */
@Service
public class AwardService implements IAwardService {

    private static final Logger log = LoggerFactory.getLogger(AwardService.class);
    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;
    @Resource
    private Map<String, IDistributeAward> distributeAwardMap;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = SendAwardMessageEvent.SendAwardMessage.builder()
                .awardId(userAwardRecordEntity.getAwardId())
                .userId(userAwardRecordEntity.getUserId())
                .orderId(userAwardRecordEntity.getOrderId())
                .awardTitle(userAwardRecordEntity.getAwardTitle())
                .build();
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);

        // 构建任务对象
        TaskEntity taskEntity = TaskEntity.builder()
                .state(TaskStateVO.create)
                .topic(sendAwardMessageEvent.getTopic())
                .messageId(sendAwardMessageEventMessage.getId())
                .message(sendAwardMessageEventMessage)
                .userId(userAwardRecordEntity.getUserId())
                .build();

        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = UserAwardRecordAggregate.builder()
                .userAwardRecordEntity(userAwardRecordEntity)
                .taskEntity(taskEntity)
                .build();

        // 存储聚合对象 - 一个事务下,用户的中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        // 奖品 KEY
        String awardKey = awardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        if (null == awardKey) {
            log.error("分发奖品;奖品ID不存在. awardKey: {}", awardKey);
            return;
        }

        // 奖品服务
        IDistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (null == distributeAward) {
            log.error("分发奖品,对应服务不存在. awardKey: {}", awardKey);
            throw new RuntimeException("分发奖品,奖品" + awardKey + "对应的服务不存在");
        }

        // 发放奖品
        distributeAward.giveOutPrizes(distributeAwardEntity);
    }
}
