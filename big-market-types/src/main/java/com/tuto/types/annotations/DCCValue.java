package com.tuto.types.annotations;

import java.lang.annotation.*;

/**
 * 注解,动态配置中心
 *
 * @author tu
 * @date 2025-02-17 上午11:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DCCValue {

    String value() default "";
}
