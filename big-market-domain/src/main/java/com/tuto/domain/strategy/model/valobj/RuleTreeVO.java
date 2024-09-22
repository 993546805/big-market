package com.tuto.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 树根
 * @author tu
 * @date 2024-09-21 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeVO {
    /** 规则树 ID **/
    private Integer treeId;
    /** 规则树名称 **/
    private String treeName;
    /** 规则树描述 **/
    private String treeDesc;
    /** 规则树根节点 **/
    private String treeRootRuleNode;
    /** 规则节点 **/
    private Map<String,RuleTreeNodeVO> treeNodeMap;
}
