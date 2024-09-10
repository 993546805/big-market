package com.tuto.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略结果实体
 * @author tu
 * @date 2024-09-09 15:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {
    private Long strategyId;
    private Integer awardId;
}
