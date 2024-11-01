package com.tuto.domain.activity.event;

import com.tuto.types.event.BaseEvent;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author tu
 * @date 2024-11-01 00:51
 */
@Component
public class AwardStockZeroMessageEvent  extends BaseEvent<AwardStockZeroMessageEvent.AwardStockZeroVo> {

    @Value("${spring.rabbitmq.topic.strategy_award_sku_stock_zero}")
    private String topic;

    @Override
    public EventMessage<AwardStockZeroVo> buildEventMessage(AwardStockZeroVo awardStockZeroVo) {
        return EventMessage.<AwardStockZeroVo>builder()
                .id(awardStockZeroVo.getStrategyId().toString())
                .timestamp(new Date())
                .data(awardStockZeroVo)
                .build();
    }

    @Override
    public String getTopic() {
        return topic;
    }


    @Data
    @Builder
    public static class AwardStockZeroVo {
        private Long strategyId;
        private Integer awardId;
    }
}
