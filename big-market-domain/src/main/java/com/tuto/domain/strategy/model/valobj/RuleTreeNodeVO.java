package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

/**
 * @author tu
 * @date 2024-09-21 11:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeNodeVO {
    /** 规则树 ID **/
    private Integer treeId;
    /** 规则 key **/
    private String ruleKey;
    /** 规则描述 **/
    private String ruleDesc;
    /** 规则值 **/
    private String ruleValue;
    /** 规则连线 **/
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
