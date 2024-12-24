package com.tuto.domain.award.repository;

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

}
