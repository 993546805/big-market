package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 抽奖活动账户流水表
 * @author tu
 * @date 2024-10-15 11:09
 */
@Data
public class RaffleActivityAccountFlow {
    /**  自增ID */
    private Long id;
    /** 用户 ID */
    private String userId;
    /** 活动 ID */
    private Long activityId;
    /** 总次数 */
    private Integer totalCount;
    /** 日次数 */
    private Integer dayCount;
    /** 月次数 */
    private Integer monthCount;
    /** 流水 ID */
    private String flowId;
    /** 流水渠道（activity-活动领取、sale-购买、redeem-兑换、free-免费赠送） */
    private String flowChannel;
    /** 业务 ID(外部透传，活动 ID,订单 ID) */
    private String bizId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
