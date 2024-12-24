package com.tuto.domain.award.service;

import com.tuto.domain.award.event.SendAwardMessageEvent;
import com.tuto.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.tuto.domain.award.model.entity.TaskEntity;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import com.tuto.domain.award.model.valobj.TaskStateVO;
import com.tuto.domain.award.repository.IAwardRepository;
import com.tuto.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-12-24 下午3:11
 */
@Service
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;
    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        // 构建消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = SendAwardMessageEvent.SendAwardMessage.builder()
                .awardId(userAwardRecordEntity.getAwardId())
                .userId(userAwardRecordEntity.getUserId())
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
}
