package com.tuto.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2025-01-03 下午2:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributeAwardEntity {
    /** 用户 ID */
    private String userId;
    /** 订单 ID */
    private String orderId;
    /** 奖品 ID */
    private Integer awardId;
    /** 奖品配置信息 */
    private String awardConfig;
}
