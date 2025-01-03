package com.tuto.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户活动账户应答对象
 *
 * @author tu
 * @date 2025-01-01 下午2:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityAccountResponseDTO {

    /** 总次数 */
    private Integer totalCount;
    /** 总次数-剩余 */
    private Integer totalCountSurplus;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
    /** 月次数 */
    private Integer monthCount;
    /** 月次数-剩余 */
    private Integer monthCountSurplus;

}
