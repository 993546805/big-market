package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author tu
 * @date 2024-10-15 10:55
 */
@Data
public class RaffleActivity {
    /** 主键 ID */
    private Long id;
    /** 活动 ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 活动描述 */
    private String activityDesc;
    /** 开始活动时间 */
    private Date beginDateTime;
    /** 结束活动时间 */
    private Date endDateTime;
    /** 库存总数 */
    private Integer stockCount;
    /** 库存剩余数 */
    private Integer stockCountSurplus;
    /** 活动数量 ID */
    private Long activityCountId;
    /** 策略  ID*/
    private Long strategyId;
    /** 活动状态 */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
