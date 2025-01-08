package com.tuto.domain.activity.model.aggregate;

import com.tuto.domain.activity.model.entity.ActivityOrderEntity;
import com.tuto.domain.activity.model.valobj.OrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tu
 * @date 2024-10-17 15:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuotaOrderAggregate {
    private String userId;
    private Long activityId;
    private Integer totalCount;
    private Integer dayCount;
    private Integer monthCount;
    private ActivityOrderEntity activityOrderEntity;

    public void setOrderState(OrderStateVO orderState) {
        this.activityOrderEntity.setState(orderState);
    }
}
