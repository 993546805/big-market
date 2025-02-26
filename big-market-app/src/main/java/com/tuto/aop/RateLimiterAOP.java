package com.tuto.aop;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.tuto.types.annotations.DCCValue;
import com.tuto.types.annotations.RateLimiterAccessInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author tu
 * @date 2025-02-20 下午6:04
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAOP {

    // 个人限制频率 1 分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();
    // 个人限频黑名单 24h -分布式业务场景,可以记录到 Redis
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @DCCValue("rateLimiterSwitch:close")
    private String rateLimiterSwitch;

    @Pointcut("@annotation(com.tuto.types.annotations.RateLimiterAccessInterceptor)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(rateLimiterAccessInterceptor)")
    public Object doRouter(ProceedingJoinPoint joinPoint, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {
        // 0. 限流开关[open 开启 close 关闭] 关闭后,不会走限流策略
        if (StringUtils.isBlank(rateLimiterSwitch) || "close".equals(rateLimiterSwitch)) {
            return joinPoint.proceed();
        }

        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("annotation RateLimiter uId is null!");
        }
        // 获取拦截字段
        String keyAttr = getAttrValue(key, joinPoint.getArgs());
        log.info("aop attr {}", keyAttr);

        // 黑名单拦截
        if (!"all".equals(keyAttr) && rateLimiterAccessInterceptor.blacklistCount() != 0
                && null != blacklist.getIfPresent(keyAttr) &&
                blacklist.getIfPresent(keyAttr) >= rateLimiterAccessInterceptor.blacklistCount()) {
            log.info("限流-黑名单拦截(24H):{}", keyAttr);
            return fallbackMethodResult(joinPoint, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 获取限流 -> Guava 缓存一分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null == rateLimiter) {
            rateLimiter = RateLimiter.create(rateLimiterAccessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }

        // 限流拦截
        if (!rateLimiter.tryAcquire()) {
            if (rateLimiterAccessInterceptor.blacklistCount() != 0) {
                if (null == blacklist.getIfPresent(keyAttr)) {
                    blacklist.put(keyAttr, 1L);
                } else {
                    blacklist.put(keyAttr, blacklist.getIfPresent(keyAttr) + 1);
                }
            }
            log.info("限流-超频次拦截: {}", keyAttr);
            return fallbackMethodResult(joinPoint, rateLimiterAccessInterceptor.fallbackMethod());
        }

        // 返回结果
        return joinPoint.proceed();
    }

    private Object fallbackMethodResult(ProceedingJoinPoint joinPoint, String fallbackMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = joinPoint.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(joinPoint.getTarget(), joinPoint.getArgs());
    }


    /**
     * 获取属性值
     *
     * @param attr 属性名
     *             例如: uId
     * @param args
     * @return
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                // fix: 使用 lombok 时,uId 这种字段的 get 方法与 idea 生成的 get 方法不同,会导致获取不到属性值,改成反射获取解决,例如: @Data 自动生成的 get 方法为 isUId()
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("获取路由属性值失败 attr: {}", attr, e);
            }
        }
        return filedValue;
    }

    /**
     * 获取对象特定的属性值,例如: uId
     *
     * @param bean 对象
     * @param name 属性名
     * @return 属性值, 不存在则返回 null
     */
    private Object getValueByName(Object bean, String name) {
        try {
            Field filed = getFiledByName(bean, name);
            if (filed == null) {
                return null;
            }
            filed.setAccessible(true);
            Object o = filed.get(bean);
            filed.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取属性,该方法同事兼顾继承类获取父类的属性
     *
     * @param bean 对象
     * @param name 属性名
     * @return 该属性对应方法
     */
    private Field getFiledByName(Object bean, String name) {
        try {
            Field field;
            try {
                field = bean.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                field = bean.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
