package com.tuto.domain.rebate.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 行为类型值对象
 * @author tu
 * @date 2024-12-27 上午10:08
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {

    SING("sign", "签到(日历)"),
    OPENAI_PAY("openai_pay", "openai 外部支付完成"),
    ;

    private final String code;
    private final String info;
}
