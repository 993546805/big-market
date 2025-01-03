package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户积分账户
 *
 * @author tu
 * @date 2025-01-03 下午3:20
 */
@Data
public class UserCreditAccount {
    /**
     * 主键 ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 总积分
     */
    private BigDecimal totalAmount;
    /**
     * 可用积分
     */
    private BigDecimal availableAmount;
    /**
     * 账户状态[open - 可用, close - 冻结]
     */
    private String accountStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
