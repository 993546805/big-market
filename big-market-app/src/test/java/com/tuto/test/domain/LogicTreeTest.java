package com.tuto.test.domain;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import com.tuto.domain.strategy.model.valobj.*;
import com.tuto.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.tuto.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author tu
 * @date 2024-09-22 12:12
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogicTreeTest {


    @Resource
    private DefaultTreeFactory defaultTreeFactory;


    /**
     * 用于测试:决策树逻辑
     * rule_lock --左--> rule_luck_award
     * --右--> rule_stock --右--> rule_luck_award
     */
    @Test
    public void test_tree_role() {
        // 构建参数
        RuleTreeNodeVO rule_lock = RuleTreeNodeVO.builder()
                .treeId(100000001)
                .ruleKey("rule_lock")
                .ruleDesc("限定用户已完成 N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {
                    {
                        add(RuleTreeNodeLineVO.builder()
                                .treeId(100000001)
                                .ruleNodeFrom("rule_lock")
                                .ruleNodeTo("rule_luck_award")
                                .ruleLimitType(RuleLimitTypeVO.EQUAL)
                                .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                                .build());

                        add(RuleTreeNodeLineVO.builder()
                                .treeId(100000001)
                                .ruleNodeFrom("rule_lock")
                                .ruleNodeTo("rule_stock")
                                .ruleLimitType(RuleLimitTypeVO.EQUAL)
                                .ruleLimitValue(RuleLogicCheckTypeVO.ALLOW)
                                .build());
                    }
                })
                .build();

        RuleTreeNodeVO rule_luck_award = RuleTreeNodeVO.builder()
                .treeId(100000001)
                .ruleKey("rule_luck_award")
                .ruleDesc("限定用户已完成 N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(null)
                .build();

        RuleTreeNodeVO rule_stock = RuleTreeNodeVO.builder()
                .treeId(100000001)
                .ruleKey("rule_stock")
                .ruleDesc("限定用户已完成 N次抽奖后解锁")
                .ruleValue("1")
                .treeNodeLineVOList(new ArrayList<RuleTreeNodeLineVO>() {{
                    add(RuleTreeNodeLineVO.builder()
                            .treeId(100000001)
                            .ruleNodeFrom("rule_stock")
                            .ruleNodeTo("rule_luck_award")
                            .ruleLimitType(RuleLimitTypeVO.EQUAL)
                            .ruleLimitValue(RuleLogicCheckTypeVO.TAKE_OVER)
                            .build());

                }
                })
                .build();


        RuleTreeVO ruleTree = new RuleTreeVO();
        ruleTree.setTreeId(100000001);
        ruleTree.setTreeName("决策树规则;增加 dall-e-3画图模型");
        ruleTree.setTreeDesc("决策树规则;增加 dall-e-e画图模型");
        ruleTree.setTreeRootRuleNode("rule_lock");
        ruleTree.setTreeNodeMap(new HashMap<String, RuleTreeNodeVO>(){{
            put("rule_lock", rule_lock);
            put("rule_luck_award", rule_luck_award);
            put("rule_stock", rule_stock);
        }});

        IDecisionTreeEngine treeComposite = defaultTreeFactory.openLogicTree(ruleTree);
        DefaultTreeFactory.StrategyAwardData data = treeComposite.process("userId", 10001L, 100);
        log.info("测试结果: {}", JSON.toJSONString(data));
    }
}
