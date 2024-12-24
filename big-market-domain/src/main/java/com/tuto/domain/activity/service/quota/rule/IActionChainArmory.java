package com.tuto.domain.activity.service.quota.rule;

/**
 * @author tu
 * @date 2024-10-17 15:39
 */
public interface IActionChainArmory {
    IActionChain next();
    IActionChain appendNext(IActionChain next);
}
