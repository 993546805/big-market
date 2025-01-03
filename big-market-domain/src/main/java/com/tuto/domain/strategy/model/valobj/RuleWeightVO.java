package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;

/**
 * @author tu
 * @date 2025-01-01 下午3:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleWeightVO {

    private String ruleValue;
    private List<Integer> awardIds;
    private List<Award> awardList;
    private Integer weight;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Award {
        private Integer awardId;
        private String awardTitle;
    }
}
