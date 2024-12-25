package com.tuto.domain.strategy.service;

import java.util.Map;

/**
 *
 * 抽奖规则接口；提供对规则的业务功能查询
 * @author tu
 * @date 2024-12-25 下午4:52
 */
public interface IRaffleRule {

    /**
     * 根据规则树 ID 集合查询奖品中加锁数量的配置 [部分奖品需要抽奖 N 次解锁]
     * @param treeIds 规则树 ID 值
     * @return  key 规则树,value rule_lock 加锁值
     */
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
