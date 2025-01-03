package com.tuto.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户积分奖品实体对象
 *
 * @author tu
 * @date 2025-01-03 下午3:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreditAwardEntity {
    /** 用户ID */
    private String userId;
    /** 积分值 */
    private BigDecimal creditAmount;
}
