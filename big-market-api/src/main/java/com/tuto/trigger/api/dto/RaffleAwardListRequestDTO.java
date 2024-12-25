package com.tuto.trigger.api.dto;

import lombok.Data;

/**
 * 抽奖策略列表请求参数
 *
 * @author tu
 * @date 2024-10-14 16:07
 */
@Data
public class RaffleAwardListRequestDTO {
    /** 活动 ID */
    private Long activityId;
    /** 用户 ID */
    private String userId;
}
