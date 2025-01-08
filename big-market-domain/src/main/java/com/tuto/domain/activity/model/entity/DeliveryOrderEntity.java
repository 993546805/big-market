package com.tuto.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 出货单实体对象
 *
 * @author tu
 * @date 2025-01-07 下午4:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderEntity {
    /**
     * 用户 ID
     */
    private String userId;
    /**
     * 业务防重 ID - 外部透传,返利,行为等唯一标识
     */
    private String outBusinessNo;
}
