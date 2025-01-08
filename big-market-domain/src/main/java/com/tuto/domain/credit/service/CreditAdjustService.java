package com.tuto.domain.credit.service;

import com.tuto.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.tuto.domain.credit.model.aggregate.TradeAggregate;
import com.tuto.domain.credit.model.entity.CreditAccountEntity;
import com.tuto.domain.credit.model.entity.CreditOrderEntity;
import com.tuto.domain.credit.model.entity.TaskEntity;
import com.tuto.domain.credit.model.entity.TradeEntity;
import com.tuto.domain.credit.model.valobj.TaskStateVO;
import com.tuto.domain.credit.repository.ICreditRepository;
import com.tuto.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 积分调额服务[正逆向,增减积分]
 *
 * @author tu
 * @date 2025-01-06 下午9:08
 */
@Slf4j
@Service
public class CreditAdjustService implements ICreditAdjustService {

    @Resource
    private ICreditRepository creditRepository;
    @Resource
    private CreditAdjustSuccessMessageEvent creditAdjustSuccessMessageEvent;

    @Override
    public String createOrder(TradeEntity tradeEntity) {
        log.info("增加账户积分额度开始 userId: {} tradeName: {} amount: {}", tradeEntity.getUserId(), tradeEntity.getTradeName(), tradeEntity.getAmount());
        // 1. 创建积分账户实体
        CreditAccountEntity creditAccountEntity = TradeAggregate.createCreditAccountEntity(
                tradeEntity.getUserId(),
                tradeEntity.getAmount()
        );

        // 2. 创建账户订单实体
        CreditOrderEntity creditOrderEntity = TradeAggregate.createCreditOrderEntity(
                tradeEntity.getUserId(),
                tradeEntity.getTradeName(),
                tradeEntity.getTradeType(),
                tradeEntity.getAmount(),
                tradeEntity.getOutBusinessNo()
        );

        // 3. 构建消息任务对象
        CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = new CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage();
        creditAdjustSuccessMessage.setUserId(tradeEntity.getUserId());
        creditAdjustSuccessMessage.setOrderId(creditOrderEntity.getOrderId());
        creditAdjustSuccessMessage.setAmount(tradeEntity.getAmount());
        creditAdjustSuccessMessage.setOutBusinessNo(tradeEntity.getOutBusinessNo());
        BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> creditAdjustSuccessMessageEventMessage = creditAdjustSuccessMessageEvent.buildEventMessage(creditAdjustSuccessMessage);
        TaskEntity taskEntity = TaskEntity.builder()
                .userId(tradeEntity.getUserId())
                .topic(creditAdjustSuccessMessageEvent.getTopic())
                .messageId(creditAdjustSuccessMessageEventMessage.getId())
                .message(creditAdjustSuccessMessageEventMessage)
                .taskState(TaskStateVO.create)
                .build();

        // 4. 构建交易聚合对象
        TradeAggregate tradeAggregate = TradeAggregate.builder()
                .userId(tradeEntity.getUserId())
                .creditAccountEntity(creditAccountEntity)
                .creditOrderEntity(creditOrderEntity)
                .taskEntity(taskEntity)
                .build();

        // 5. 保存积分交易订单
        creditRepository.saveUserCreditTradeOrder(tradeAggregate);
        log.info("增加账户积分额度完成 userId: {} orderId: {}", tradeEntity.getUserId(), creditOrderEntity.getOrderId());

        return creditOrderEntity.getOrderId();
    }
}
