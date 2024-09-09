package com.tuto.domain.strategy.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tu
 * @date 2024-09-06 17:00
 */
@Data
public class StrategyAwardEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID - 内部流转使用 */
    private Integer awardId;
    /** 奖品库存总量 */
    private Integer awardCount;
    /** 奖品库存剩余 */
    private Integer awardCountSurplus;
    /** 奖品中奖概率 */
    private BigDecimal awardRate;
}
