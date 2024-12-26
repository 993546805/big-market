package com.tuto.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tuto.domain.activity.event.AwardStockZeroMessageEvent;
import com.tuto.domain.strategy.service.IRaffleStock;
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
//@Component
public class StrategyAwardStockZeroCustomer {

    @Value("${spring.rabbitmq.topic.strategy_award_sku_stock_zero}")
    private String topic;

    @Resource
    private IRaffleStock raffleStock;

    @RabbitListener(queuesToDeclare = @Queue(value = "strategy_award_sku_stock_zero"))
    public void listener(String message) {
        try {
            log.info("监听策略奖品库存消耗为 0 消息 topic:{} message:{}", topic, message);
            // 转换对象
            BaseEvent.EventMessage<AwardStockZeroMessageEvent.AwardStockZeroVo> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<AwardStockZeroMessageEvent.AwardStockZeroVo>>() {
            }.getType());
            AwardStockZeroMessageEvent.AwardStockZeroVo awardStockZeroVo = eventMessage.getData();
            // 更新库存
            raffleStock.clearStrategyAwardStock(awardStockZeroVo.getStrategyId(), awardStockZeroVo.getAwardId());
            // 清空队列
            raffleStock.clearQueueValue();
        } catch (Exception e) {
            log.error("监听策略奖品库存消耗为 0 消息异常 topic:{} message:{}", topic, message, e);
            throw e;
        }
    }
}
