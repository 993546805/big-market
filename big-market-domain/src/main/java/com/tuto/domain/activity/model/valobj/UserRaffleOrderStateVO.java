package com.tuto.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 * @author tu
 * @date 2024-11-05 16:42
 */
@Getter
@AllArgsConstructor
public enum UserRaffleOrderStateVO {

    CREATE("create", "创建"),
    USED("used", "已使用"),
    CANCEL("cancel", "已作废"),
    ;

    private final String code;
    private final String info;

    public static UserRaffleOrderStateVO of(String orderState) {
        for (UserRaffleOrderStateVO value : UserRaffleOrderStateVO.values()) {
            if (value.getCode().equals(orderState)) {
                return value;
            }
        }
        return null;
    }
}
