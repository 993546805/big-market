package com.tuto.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2024-10-17 11:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityShopCartEntity {

    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;
}
