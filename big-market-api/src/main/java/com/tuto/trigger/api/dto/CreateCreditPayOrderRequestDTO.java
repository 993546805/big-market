package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建积分支付订单参数
 *
 * @author tu
 * @date 2025-01-08 下午4:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditPayOrderRequestDTO {
    /**
     * 用户id
     */
    private String userId;
    /**
     * sku
     */
    private Long sku;
}
