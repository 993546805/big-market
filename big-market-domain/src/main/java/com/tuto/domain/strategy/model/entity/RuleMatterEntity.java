package com.tuto.domain.strategy.model.entity;

import lombok.Data;

/**
 * 规则物料实体对象,用于过滤规则的必要参数信息
 * @author tu
 * @date 2024-09-10 21:57
 */
@Data
public class RuleMatterEntity {
    /** 用户 ID **/
    private String userId;
    /** 策略 ID **/
    private Long strategyId;
    /** 抽奖奖品 ID[规则类型为策略,则不需要奖品 ID] **/
    private Integer awardId;
    /** 抽奖规则类型[rule_random - 随机值计算; rule_lock - 抽奖几次解锁; rule_luck_award - 幸运奖(兜底奖品)] **/
    private String ruleModel;
}
