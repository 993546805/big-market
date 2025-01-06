package com.tuto.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易类型枚举
 *
 * @author tu
 * @date 2025-01-06 下午9:06
 */
@Getter
@AllArgsConstructor
public enum TradeTypeVO {

    FORWARD("forward", "正向交易, + 积分"),
    REVERSE("reverse", "反向交易, - 积分"),
    ;

    private final String code;
    private final String desc;
}
