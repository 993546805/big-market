package com.tuto.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tuto.domain.activity.model.entity.DeliveryOrderEntity;
import com.tuto.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.tuto.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.tuto.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 积分调整成功信息
 *
 * @author tu
 * @date 2025-01-07 下午6:11
 */
@Slf4j
@Component
public class CreditAdjustSuccessCustomer {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    public String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue("${spring.rabbitmq.topic.credit_adjust_success}"))
    public void listener(String message) {
        try {
            log.info("监听积分账户调整成功信息,进行交易商品发货 topic: {} message: {}", topic, message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {}.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();

            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
            deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());

            raffleActivityAccountQuotaService.updateOrder(deliveryOrderEntity);
        } catch (Exception e) {
            log.error("监听积分账户调整成功信息,进行交易商品发货失败 topic: {} message: {}", topic, message,e);
            throw e;
        }
    }
}
