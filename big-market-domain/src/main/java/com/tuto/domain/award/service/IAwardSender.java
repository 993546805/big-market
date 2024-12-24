package com.tuto.domain.award.service;

/**
 * @author tu
 * @date 2024-12-24 下午8:25
 */
public interface IAwardSender {

    /**
     * 使用模版
     * 1. 查询奖品信息
     * 2. 策略模式:根据奖品信息配置选取不同的发放执行器执行
     *
     * @param awardId
     * @param awardTitle
     * @param userId
     */
    void sendAward(String awardId, String awardTitle, String userId);
}
