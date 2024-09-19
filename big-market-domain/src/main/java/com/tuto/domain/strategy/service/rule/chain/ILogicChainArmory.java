package com.tuto.domain.strategy.service.rule.chain;

/**
 * 责任链装配
 * @author tu
 * @date 2024-09-19 20:18
 */
public interface ILogicChainArmory {
    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);

}
