package com.tuto.domain.credit.model.entity;

import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author tu
 * @date 2025-01-06 下午9:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditOrderEntity {
    /** 用户 ID */
    private String userId;
    /** 订单 ID */
    private String orderId;
    /** 交易名称 */
    private TradeNameVO tradeName;
    /** 交易类型; forward-正向 reverse-反向 */
    private TradeTypeVO tradeType;
    /** 交易金额 */
    private BigDecimal tradeAmount;
    /** 外部订单号 透传-防重*/
    private String outBusinessNo;
}
