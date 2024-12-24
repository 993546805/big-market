package com.tuto.trigger.api.dto;

import lombok.Data;

/**
 * 活动抽奖请求对象
 * @author tu
 * @date 2024-12-24 下午8:34
 */
@Data
public class ActivityDrawRequestDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
