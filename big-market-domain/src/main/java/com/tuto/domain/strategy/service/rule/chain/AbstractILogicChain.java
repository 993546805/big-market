package com.tuto.domain.strategy.service.rule.chain;

/**
 * @author tu
 * @date 2024-09-19 20:21
 */
public abstract class AbstractILogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }


    protected abstract String ruleModel();
}
