package com.tuto.domain.award.service.distribute;

import com.tuto.domain.award.model.entity.DistributeAwardEntity;

/**
 * 分发奖品接口
 */
public interface IDistributeAward {

    void giveOutPrizes(DistributeAwardEntity distributeAwardEntity);
}
