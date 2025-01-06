package com.tuto.domain.credit.model.entity;

import com.tuto.domain.credit.model.valobj.TradeNameVO;
import com.tuto.domain.credit.model.valobj.TradeTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 交易实体对象
 *
 * @author tu
 * @date 2025-01-06 下午8:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeEntity {
    /** 用户ID */
    private String userId;
    /** 交易名称 */
    private TradeNameVO tradeName;
    /** 交易类型; forward-正向, reverse-反向 */
    private TradeTypeVO tradeType;
    /** 交易金额 */
    private BigDecimal amount;
    /** 业务防重 ID - 外部透传. 返利 行为等唯一标识 */
    private String outBusinessNo;
}
