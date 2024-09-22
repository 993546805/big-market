package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则限定枚举值
 * @author tu
 * @date 2024-09-21 10:59
 */
@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {

    EQUAL(1, "等于"),
    GT(2, "大于"),
    LT(3, "小于"),
    GE(4, "大于等于"),
    LE(5, "小于等于"),
    ENUM(6, "枚举"),
    ;

    private final Integer code;
    private final String info;
}
