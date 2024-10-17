package com.tuto.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2024-10-17 15:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuRechargeEntity {
    private String userId;
    private Long sku;
    private String outBusinessNo;
}
