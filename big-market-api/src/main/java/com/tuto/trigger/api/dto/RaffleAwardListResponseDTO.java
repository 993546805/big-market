package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖奖品列表，应答对象
 * @author tu
 * @date 2024-10-14 16:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardListResponseDTO {
    // 奖品ID
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则 - 抽奖 N次后解锁,为配置则为空
    private Integer awardRuleLockCount;
    // 奖品是否解锁 - true 已解锁 false 未解锁
    private Boolean isAwardUnlock;
    // 等待解锁次数 - 规定的抽奖 N 次解锁减去用户已抽奖次数
    private Integer waitUnlockCount;
}
