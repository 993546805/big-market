package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author tu
 * @date 2024-11-01 16:28
 */
@Data
public class UserAwardRecord {
    /** 自增 ID */
    private Long id;
    /** 用户 ID */
    private Long userId;
    /** 活动 ID */
    private Long activityId;
    /** 策略 ID */
    private Long strategyId;
    /** 抽奖订单ID*/
    private String orderId;
    /** 奖品 ID */
    private Long awardId;
    /** 奖品标题 */
    private String awardTitle;
    /** 中奖时间 */
    private Long awardTime;
    /** 状态： create 创建 completed 完成 */
    private Integer status;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
