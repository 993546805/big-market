package com.tuto.domain.rebate.model.aggregate;

import com.tuto.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.tuto.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.N;

/**
 * 行为返利聚合对象
 *
 * @author tu
 * @date 2024-12-30 下午9:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorRebateAggregate {

    /** 用户 ID */
    private String userId;
    /** 行为返利订单实体对象 */
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    /** 任务实体对象 */
    private TaskEntity task;
}
