package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖活动账户
 *
 * @author tu
 * @date 2024-10-15 11:02
 */
@Data
public class RaffleActivityAccount {
    /** 自增 ID */
    private Long id;
    /** 用户 ID */
    private String userId;
    /** 活动 ID */
    private Long activityId;
    /** 总数 */
    private Integer totalCount;
    /** 总剩余数 */
    private Integer totalCountSurplus;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
    /** 月次数 */
    private Integer monthCount;
    /** 月次数-剩余 */
    private Integer monthCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
