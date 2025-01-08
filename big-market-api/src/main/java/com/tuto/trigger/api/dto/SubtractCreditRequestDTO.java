package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 扣减积分请求参数
 * @author tu
 * @date 2025-01-08 下午4:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtractCreditRequestDTO {
    /** 用户id */
    private String userId;
    /** 扣减金额 */
    private BigDecimal amount;
    /** 外部业务号 */
    private String outBusinessNo;
}
