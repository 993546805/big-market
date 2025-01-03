package com.tuto.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tuto.domain.award.event.SendAwardMessageEvent;
import com.tuto.domain.award.model.entity.DistributeAwardEntity;
import com.tuto.domain.award.service.IAwardService;
import com.tuto.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-12-24 下午7:35
 */
@Slf4j
@Component
public class SendAwardCustomer {


    @Value("${spring.rabbitmq.topic.send_award}")
    private String topic;
    @Resource
    private IAwardService awardService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_award}"))
    public void listener(String message){
        try {
            log.info("监听用户奖品发送消息 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage>>() {}.getType());
            SendAwardMessageEvent.SendAwardMessage awardMessage = eventMessage.getData();

            // 发放奖品
            DistributeAwardEntity distributeAwardEntity = DistributeAwardEntity.builder()
                    .userId(awardMessage.getUserId())
                    .orderId(awardMessage.getOrderId())
                    .awardId(awardMessage.getAwardId())
                    .awardConfig(awardMessage.getAwardConfig())
                    .build();

            awardService.distributeAward(distributeAwardEntity);
        } catch (Exception e) {
            log.error("监听用户奖品发送消息,消费失败 topic: {} message: {}", topic, message);
            throw e;
        }
    }
}
