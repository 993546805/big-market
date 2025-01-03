package com.tuto.domain.award.repository;

import com.tuto.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.tuto.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * 奖品仓储层接口
 */
public interface IAwardRepository {
    /**
     * 保存用户中奖记录
     *
     * @param userAwardRecordAggregate 用户中奖记录聚合对象
     */
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    /**
     * 保存发奖记录
     *
     * @param giveOutPrizesAggregate 发奖记录聚合对象
     */
    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    /**
     * 查询奖品配置
     * 根据奖品ID查询对应的奖品配置信息
     *
     * @param awardId 奖品ID
     * @return 返回奖品的配置信息字符串如果找不到对应的奖品配置，返回null或空字符串
     */
    String queryAwardConfig(Integer awardId);

    /**
     * 查询奖品Key
     * @param awardId 奖品ID
     * @return 返回奖品Key, 如果找不到对应的奖品配置，返回null或空字符串
     */
    String queryAwardKey(Integer awardId);
}

