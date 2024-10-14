package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2024-10-09 17:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {

    // 策略ID
    private Long strategyId;
    // 奖品ID
    private Integer awardId;
}
