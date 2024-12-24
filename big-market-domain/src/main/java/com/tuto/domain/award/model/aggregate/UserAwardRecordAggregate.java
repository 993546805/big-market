package com.tuto.domain.award.model.aggregate;

import com.tuto.domain.award.model.entity.TaskEntity;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户奖品记录聚合对象 [聚合代表一个事务]
 * @author tu
 * @date 2024-12-24 下午3:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAwardRecordAggregate {

    private UserAwardRecordEntity userAwardRecordEntity;

    private TaskEntity taskEntity;

}
