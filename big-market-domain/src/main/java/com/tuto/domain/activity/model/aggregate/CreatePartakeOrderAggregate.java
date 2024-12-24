package com.tuto.domain.activity.model.aggregate;

import com.tuto.domain.activity.model.entity.ActivityAccountDayEntity;
import com.tuto.domain.activity.model.entity.ActivityAccountEntity;
import com.tuto.domain.activity.model.entity.ActivityAccountMonthEntity;
import com.tuto.domain.activity.model.entity.UserRaffleOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.Jar;

/**
 * @author tu
 * @date 2024-11-05 17:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartakeOrderAggregate {
    private String userId;
    private Long activityId;
    private UserRaffleOrderEntity userRaffleOrderEntity;
    private ActivityAccountEntity activityAccountEntity;
    private boolean isExistAccountMonth;
    private ActivityAccountMonthEntity activityAccountMonthEntity;
    private boolean isExistAccountDay;
    private ActivityAccountDayEntity activityAccountDayEntity;

}
