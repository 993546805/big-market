package com.tuto.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2024-10-31 23:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySkuStockKeyVO {
    /** 商品 ID */
    private Long sku;
    /** 活动ID */
    private Long activityId;
}
