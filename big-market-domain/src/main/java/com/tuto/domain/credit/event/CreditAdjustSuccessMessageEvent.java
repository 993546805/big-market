package com.tuto.domain.credit.event;

import com.tuto.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 积分账户调整成功消息[充值,支付 成功消息]
 * @author tu
 * @date 2025-01-07 下午5:47
 */
@Component
public class CreditAdjustSuccessMessageEvent extends BaseEvent<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> {

    @Value("${spring.rabbitmq.topic.credit_adjust_success}")
    private String topic;


    @Override
    public EventMessage<CreditAdjustSuccessMessage> buildEventMessage(CreditAdjustSuccessMessage creditAdjustSuccessMessage) {
        return EventMessage.<CreditAdjustSuccessMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(creditAdjustSuccessMessage)
                .build();
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditAdjustSuccessMessage {
        /** 用户 ID */
        private String userId;
        /** 订单ID */
        private String orderId;
        /** 交易金额 */
        private BigDecimal amount;
        /** 业务防重 ID  */
        private String outBusinessNo;
    }
}
