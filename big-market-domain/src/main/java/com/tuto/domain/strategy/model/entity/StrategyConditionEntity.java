package com.tuto.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略条件实体
 * @author tu
 * @date 2024-09-09 16:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyConditionEntity {
    private String userId;
    private Long strategyId;
}
