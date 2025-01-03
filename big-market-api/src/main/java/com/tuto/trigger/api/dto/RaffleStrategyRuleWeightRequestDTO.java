package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖策略规则权重请求对象
 * @author tu
 * @date 2025-01-01 下午3:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleStrategyRuleWeightRequestDTO {
     private String userId;
     private Long activityId;
}
