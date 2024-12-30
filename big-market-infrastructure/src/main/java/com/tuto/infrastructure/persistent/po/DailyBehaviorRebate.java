package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 日常行为返利活动配置
 *
 * @author tu
 * @date 2024-12-30 下午10:01
 */
@Data
public class DailyBehaviorRebate {
    /** 自增 ID */
    private Long id;
    /** 行为类型 */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型 */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
    /** 状态 */
    private String status;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}

