package com.tuto.domain.credit.model.aggregate;

import com.tuto.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.tuto.domain.credit.model.entity.CreditAccountEntity;
import com.tuto.domain.credit.model.entity.CreditOrderEntity;
import com.tuto.domain.credit.model.entity.TaskEntity;
import com.tuto.domain.credit.model.valobj.TaskStateVO;
import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import com.tuto.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

/**
 * 交易聚合对象
 *
 * @author tu
 * @date 2025-01-06 下午9:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeAggregate {
    /** 用户 ID */
    private String userId;
    /** 积分账户实体 */
    private CreditAccountEntity creditAccountEntity;
    /** 积分订单实体 */
    private CreditOrderEntity creditOrderEntity;
    // 任务实体 - 补偿 MQ 消息
    private TaskEntity taskEntity;

    public static CreditAccountEntity createCreditAccountEntity(String userId, BigDecimal adjustAmount){
        return CreditAccountEntity.builder()
                .userId(userId)
                .adjustAmount(adjustAmount)
                .build();
    }

    public static CreditOrderEntity createCreditOrderEntity(String userId,
                                                            TradeNameVO tradeName,
                                                            TradeTypeVO tradeType,
                                                            BigDecimal tradeAmount,
                                                            String outBusinessNo){
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeName)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();
    }


    public static TaskEntity createTaskEntity(String userId, String topic, String messageId, BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> message) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userId);
        taskEntity.setTopic(topic);
        taskEntity.setMessageId(messageId);
        taskEntity.setMessage(message);
        taskEntity.setTaskState(TaskStateVO.create);
        return taskEntity;
    }
}
