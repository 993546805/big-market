package com.tuto.domain.award.service;

import com.tuto.domain.award.model.entity.DistributeAwardEntity;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;

/**
 * 奖品服务
 */
public interface IAwardService {
    /**
     * 保存用户奖品记录
     * @param userAwardRecordEntity 用户奖品记录实体
     */
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);

    /**
     * 分发奖品
     * @param distributeAwardEntity 分发奖品实体
     */
    void distributeAward(DistributeAwardEntity distributeAwardEntity);
}
