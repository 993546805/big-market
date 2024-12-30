package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 用户行为返利订单
 * @author tu
 * @date 2024-12-30 下午10:09
 */
@Data
public class UserBehaviorRebateOrder {
    /** 自增 ID */
    private Long id;
    /** 用户 ID */
    private String userId;
    /** 订单 ID */
    private String orderId;
    /** 行为类型 */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型 */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
    /** 业务 ID */
    private String bizId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
