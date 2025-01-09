package com.tuto.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author tu
 * @date 2025-01-08 下午6:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuProductEntity {
    /**
     * 商品 sku
     */
    private Long sku;
    /**
     * 活动 ID
     */
    private Long activityId;
    /**
     * 活动个人参与次数
     */
    private Long activityCountId;
    /**
     * 库存总量
     */
    private Integer stockCount;
    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;
    /**
     * 商品金额[积分]
     */
    private BigDecimal productAmount;
    /**
     * 活动配置的次数 - 购买商品后可以获得的次数
     */
    private ActivityCount activityCount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityCount {
        /**
         * 活动个人参与次数
         */
        private Integer totalCount;
        /**
         * 活动每日参与次数
         */
        private Integer dayCount;
        /**
         * 活动每月参与次数
         */
        private Integer monthCount;
    }
}
