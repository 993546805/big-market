package com.tuto.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tuto.domain.activity.service.ISkuStock;
import com.tuto.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-10-31 23:43
 */
@Slf4j
@Component
public class ActivitySkuStockZeroCustomer {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @Resource
    private ISkuStock skuStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
    public void listener(String message) {
        try {
            log.info("监听活动 sku 库存消耗为 0 消息 topic:{} message:{}", topic, message);
            // 转换对象
            BaseEvent.EventMessage<Long> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Long>>() {
            }.getType());
            Long sku = eventMessage.getData();
            // 更新库存
            skuStock.clearActivitySkuStock(sku);
            // 清空队列
            skuStock.clearQueueValue();
        } catch (Exception e) {
            log.error("监听活动 sku 库存消耗为 0 消息异常 topic:{} message:{}", topic, message, e);
            throw e;
        }
    }
}
