package com.tuto.domain.strategy.service;

import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.model.entity.RuleActionEntity;
import com.tuto.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.tuto.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.tuto.domain.strategy.repository.IStrategyRepository;
import com.tuto.domain.strategy.service.armory.IStrategyDispatch;
import com.tuto.domain.strategy.service.rule.chain.ILogicChain;
import com.tuto.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 抽奖策略抽象类，定义抽奖的标准流程
 *
 * @author tu
 * @date 2024-09-11 21:32
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    private final DefaultChainFactory defaultChainFactory;
    // 策略仓储服务 -> domain层像打出,仓储层提供米面粮油
    protected IStrategyRepository repository;
    // 策略调度服务 -> 只负责抽奖的处理,通过新增接口的方式,隔离职责,不需要使用方关心或者调用抽奖的初始化
    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1.参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2.获取责任链
        ILogicChain chain = defaultChainFactory.openLogicChain(strategyId);

        // 3.通过责任链抽奖,获取奖品ID
        Integer awardId = chain.logic(userId, strategyId);

        // 4.查询奖品规则[抽奖中(拿到奖品 ID 过滤规则) 抽奖后(扣减库存后过滤,抽奖中拦截和无库存走兜底)]
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        // 5.抽奖中 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(RaffleFactorEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());

        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())) {
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);


}
