package com.tuto.domain.award.service.distribute.impl;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.award.model.entity.DistributeAwardEntity;
import com.tuto.domain.award.service.distribute.IDistributeAward;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tu
 * @date 2025-02-26 下午4:47
 */
@Component("openai_model")
@Slf4j
public class OpenAIModelAward implements IDistributeAward {

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        log.info("模拟分发了 openai_model 类型奖品: {}", JSON.toJSONString(distributeAwardEntity));
    }
}
