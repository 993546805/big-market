package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tu
 * @date 2025-01-01 下午3:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleStrategyRuleWeightResponseDTO {
    // 权重规则配置的抽奖次数
    private Integer ruleWeightCount;
    // 用户在一个活动下完成的总抽奖次数
    private Integer userTotalRaffleCount;
    // 当前权重可抽奖范围
    private List<StrategyAward> strategyAwardList;

    @Data
    public static class StrategyAward {
        // 奖品 ID
        private Integer awardId;
        // 奖品标题
        private String awardTitle;
    }
}
