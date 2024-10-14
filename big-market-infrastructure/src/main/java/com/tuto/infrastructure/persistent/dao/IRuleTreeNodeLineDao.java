package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tu
 * @date 2024-09-22 13:24
 */
@Mapper
public interface IRuleTreeNodeLineDao {
    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
