package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖活动单
 *
 * @author tu
 * @date 2024-10-15 11:02
 */
@Data
public class RaffleActivityOrder {
    /** 自增 ID */
    private Long id;
    /** 用户 ID */
    private String userId;
    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 策略 ID */
    private Long strategyId;
    /** 订单 ID */
    private String orderId;
    /** 订单时间 */
    private Date orderTime;
    /** 状态;not_used,used,expire */
    private String state;
    /** 业务防重 ID */
    private String outBusinessNo;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
