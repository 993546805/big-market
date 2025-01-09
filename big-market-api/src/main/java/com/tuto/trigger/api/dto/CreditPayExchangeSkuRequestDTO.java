package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2025-01-08 下午5:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditPayExchangeSkuRequestDTO {
    private String userId;
    private Long sku;
}
