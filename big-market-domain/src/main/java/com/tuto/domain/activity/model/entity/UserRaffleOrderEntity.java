package com.tuto.domain.activity.model.entity;

import com.tuto.domain.activity.model.valobj.UserRaffleOrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;

import java.util.Date;

/**
 * @author tu
 * @date 2024-11-05 16:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRaffleOrderEntity {

    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 策略ID */
    private Long strategyId;
    /** 订单ID */
    private String orderId;
    /** 订单事件 */
    private Date orderTime;
    /** 订单状态; create-创建 used-已使用 cancel-已作废 */
    private UserRaffleOrderStateVO orderState;
}
