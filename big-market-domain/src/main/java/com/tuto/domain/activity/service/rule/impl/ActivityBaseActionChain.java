package com.tuto.domain.activity.service.rule.impl;

import com.tuto.domain.activity.model.entity.ActivityCountEntity;
import com.tuto.domain.activity.model.entity.ActivityEntity;
import com.tuto.domain.activity.model.entity.ActivitySkuEntity;
import com.tuto.domain.activity.model.valobj.ActivityStateVO;
import com.tuto.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author tu
 * @date 2024-10-17 15:43
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {


    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        // 有效期限
        Date beginDateTime = activityEntity.getBeginDateTime();
        Date endDateTime = activityEntity.getEndDateTime();
        Date now = new Date();
        boolean timeSuccess = now.after(beginDateTime) && now.before(endDateTime);

        // 状态
        boolean stateSuccess = ActivityStateVO.open.equals(activityEntity.getState());

        if (timeSuccess && stateSuccess) {
            return next().action(activitySkuEntity, activityEntity, activityCountEntity);
        }

        return false;
    }
}
