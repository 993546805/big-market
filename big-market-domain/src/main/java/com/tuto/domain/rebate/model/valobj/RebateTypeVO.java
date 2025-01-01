package com.tuto.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tu
 * @date 2025-01-01 下午12:24
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {

    SKU("sku","活动库存充值商品"),
    INTEGAL("integral", "用户活动积分"),;

    private final String code;
    private final String info;
}
