package com.tuto.domain.rebate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行为返利订单实体对象
 * @author tu
 * @date 2024-12-30 下午9:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateOrderEntity {
    /** 用户 ID */
    private String userId;
    /** 订单 ID */
    private String orderId;
    /** 行为类型 (sign 签到 openai_pay 支付) */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型 (sku 活动库存充值商品 integral 用户活动积分)*/
    private String rebateType;
    /** 返利配置[ sku 积分值] */
    private String rebateConfig;
    /** 业务 ID - 拼接的唯一值 */
    private String bizId;
}
