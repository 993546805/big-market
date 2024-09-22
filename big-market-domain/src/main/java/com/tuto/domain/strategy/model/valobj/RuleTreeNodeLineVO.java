package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author tu
 * @date 2024-09-21 11:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeNodeLineVO {
    /** 规则树 ID **/
    private Integer treeId;
    /** 规则 Key节点  From**/
    private String ruleNodeFrom;
    /** 规则 Key节点 to **/
    private String ruleNodeTo;
    /** 规则限定类型 **/
    private RuleLimitTypeVO ruleLimitType;
    /** 限定值 (到下一个节点) **/
    private RuleLogicCheckTypeVO ruleLimitValue;
}
