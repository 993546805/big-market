package com.tuto.trigger.api;


import com.tuto.trigger.api.dto.*;
import com.tuto.types.model.Response;

import java.util.List;

/**
 * 抽奖服务接口
 * @author tu
 * @date 2024-10-14 16:01
 */
public interface IRaffleStrategyService {

    /**
     * 策略装配接口
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     *
     * @param req 抽奖奖品列表查询请求参数
     * @return 奖品列表数据
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO req);

    /**
     * 抽奖接口
     * @param req 抽奖请求参数
     * @return 抽奖结果
     */
    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO req);

    /**
     * 查询抽奖策略规则权重
     * @param req 查询请求参数
     * @return
     */
    Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(UserActivityAccountRequestDTO req);
}
