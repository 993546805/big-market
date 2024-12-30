package com.tuto.domain.rebate.model.entity;

import com.tuto.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 行为实体对象
 * @author tu
 * @date 2024-12-26 下午5:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorEntity {
    /** 用户 ID */
    private String userId;
    /** 行为类型; sign 签到 openai_pay 支付 */
    private BehaviorTypeVO behaviorTypeVO;
    /** 业务 ID: 签到则是日期字符串,支付则是外部的业务 ID */
    private String outBusinessNo;

}
