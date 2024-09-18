package com.tuto.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖奖品实体
 * @author tu
 * @date 2024-09-10 21:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {
    /** 策略 ID **/
    private Long strategyId;
    /** 奖品 ID **/
    private Integer awardId;
    /** 奖品对接标识 - 每一个都对应一个发奖策略 **/
    private String awardKey;
    /** 奖品配置信息 **/
    private String awardConfig;
    /** 奖品内容描述 **/
    private String awardDesc;
}
