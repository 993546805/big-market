package com.tuto.domain.rebate.repository;

import com.tuto.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.tuto.domain.rebate.model.valobj.BehaviorTypeVO;
import com.tuto.domain.rebate.model.valobj.DailyBehaviorRebateVO;

import java.util.List;

/**
 * 行为返利仓储层接口
 *
 * @author tu
 * @date 2024-12-26 下午5:37
 */
public interface IBehaviorRebateRepository {
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
