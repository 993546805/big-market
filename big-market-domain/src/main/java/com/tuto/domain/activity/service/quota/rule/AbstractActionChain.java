package com.tuto.domain.activity.service.quota.rule;

/**
 * @author tu
 * @date 2024-10-17 15:39
 */
public abstract class AbstractActionChain implements IActionChain {

    private IActionChain next;

    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
