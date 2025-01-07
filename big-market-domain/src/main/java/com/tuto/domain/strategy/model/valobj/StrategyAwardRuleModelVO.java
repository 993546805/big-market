package com.tuto.domain.strategy.model.valobj;

import com.tuto.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖策略规则规则规则值对象;没有唯一ID,仅限于从数据库查询对象
 * @author tu
 * @date 2024-09-19 19:18
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;


}
