package com.tuto.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 抽奖因子实体
 * @author tu
 * @date 2024-09-10 21:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleFactorEntity {
    /** 用户ID **/
    private String userId;
    /** 策略ID **/
    private Long strategyId;
    /** 奖品ID */
    private Integer awardId;
    /** 活动结束时间 */
    private Date endDateTime;
}
