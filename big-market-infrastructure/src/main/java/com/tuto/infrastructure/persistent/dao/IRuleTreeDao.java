package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.Award;
import com.tuto.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);
}
