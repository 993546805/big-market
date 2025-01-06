package com.tuto.domain.credit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 创建积分账户实体类
 * @author tu
 * @date 2025-01-06 下午9:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAccountEntity {

    /** 用户 ID */
    private String userId;
    /** 可用积分 */
    private BigDecimal adjustAmount;
}
