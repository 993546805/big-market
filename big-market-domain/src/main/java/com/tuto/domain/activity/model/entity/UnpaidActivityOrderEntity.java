package com.tuto.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 未支付活动订单实体
 *
 * @author tu
 * @date 2025-01-08 下午5:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidActivityOrderEntity {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 订单 ID
     */
    private String orderId;
    /**
     * 外部业务号 - 外部透传,业务防重
     */
    private String outBusinessNo;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;

}
