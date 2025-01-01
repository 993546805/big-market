package com.tuto.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 抽奖活动账户(月)
 *
 * @author tu
 * @date 2024-10-15 11:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityAccountMonth {

    private final SimpleDateFormat monthFormatDay = new SimpleDateFormat("yyyy-MM");

    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 日期（yyyy-mm-dd） */
    private String month;
    /** 日次数 */
    private Integer monthCount;
    /** 日次数-剩余 */
    private Integer monthCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;


    public String currentMonth() {
        return monthFormatDay.format(new Date());
    }
}
