package com.tuto.trigger.api.dto;

import lombok.Data;

/**
 * 用户活动账户信息请求对象
 * @author tu
 * @date 2025-01-01 下午2:29
 */
@Data
public class UserActivityAccountRequestDTO {
    /** 活动 ID */
    private Long activityId;
    /** 用户 ID */
    private String userId;
}
