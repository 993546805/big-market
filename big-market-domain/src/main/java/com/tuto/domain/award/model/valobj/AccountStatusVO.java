package com.tuto.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户状态枚举
 *
 * @author tu
 * @date 2025-01-03 下午3:24
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {
    open("open", "开启"),
    close("close", "关闭"),
    ;
    private final String code;
    private final String desc;
}
