package com.tuto.trigger.listener;

/**
 * 返利消息消费者
 *
 * @author tu
 * @date 2025-01-01 下午12:17
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tuto.domain.activity.model.entity.SkuRechargeEntity;
import com.tuto.domain.activity.model.valobj.OrderTradeTypeVO;
import com.tuto.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.tuto.domain.credit.model.entity.TradeEntity;
import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import com.tuto.domain.credit.service.ICreditAdjustService;
import com.tuto.domain.rebate.event.SendRebateMessageEvent;
import com.tuto.domain.rebate.model.valobj.RebateTypeVO;
import com.tuto.types.event.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class RebateMessageCustomer {
    private static final Logger log = LoggerFactory.getLogger(RebateMessageCustomer.class);
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private ICreditAdjustService creditIncreaseService;

    @RabbitListener(queuesToDeclare = @Queue(value = "${spring.rabbitmq.topic.send_rebate}"))
    public void listener(String message) {
        try {
            log.info("监听用户行为返利信息 topic: {} message: {}", topic, message);
            // 1. 转换消息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage>>() {}.getType());
            SendRebateMessageEvent.RebateMessage rebateMessage = eventMessage.getData();

            // 2. 入账奖励
            switch (rebateMessage.getRebateType()) {
                case "sku":
                    SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
                    skuRechargeEntity.setUserId(rebateMessage.getUserId());
                    skuRechargeEntity.setSku(Long.valueOf(rebateMessage.getRebateConfig()));
                    skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    skuRechargeEntity.setOrderTradeType(OrderTradeTypeVO.rebate_no_pay_trade);
                    raffleActivityAccountQuotaService.createOrder(skuRechargeEntity);
                    break;
                case "integral":
                    TradeEntity tradeEntity = new TradeEntity();
                    tradeEntity.setUserId(rebateMessage.getUserId());
                    tradeEntity.setTradeName(TradeNameVO.REBATE);
                    tradeEntity.setTradeType(TradeTypeVO.FORWARD);
                    tradeEntity.setAmount(new BigDecimal(rebateMessage.getRebateConfig()));
                    tradeEntity.setOutBusinessNo(rebateMessage.getBizId());
                    creditIncreaseService.createOrder(tradeEntity);
                    break;
            }
        } catch (Exception e) {
            log.error("监听用户行为返利消息, 消费失败 topic: {} message: {} ", topic, message, e);
            throw e;
        }
    }
}
