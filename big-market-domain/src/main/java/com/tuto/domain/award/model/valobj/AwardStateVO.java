package com.tuto.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author tu
 * @date 2024-12-24 下午3:20
 */
@Getter
@AllArgsConstructor
public enum  AwardStateVO {
    create("create", "创建"),
    complete("complete", "发奖完成"),
    fail("fail", "发奖失败"),
            ;

    private final String code;
    private final String desc;
}
