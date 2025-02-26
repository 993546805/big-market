package com.tuto.types.annotations;

import java.lang.annotation.*;

/**
 * @author tu
 * @date 2025-02-20 下午6:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiterAccessInterceptor {

    /** 用哪个字段作为拦截标识, 未配置则默认走全部 */
    String key() default "all";

    /** 限制频次(每秒请求次数) */
    double permitsPerSecond();

    /** 黑名单拦截(多少次限制后加入黑名单) 0 不限制 */
    int blacklistCount() default 0;

    /** 拦截后的执行方法 */
    String fallbackMethod();
}
