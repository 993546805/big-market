package com.tuto.trigger.api;

import com.tuto.trigger.api.dto.ActivityDrawRequestDTO;
import com.tuto.trigger.api.dto.ActivityDrawResponseDTO;
import com.tuto.types.model.Response;

/**
 * 抽奖活动接口
 */
public interface IRaffleActivityService {

    /**
     * 活动装配,数据预热缓存
     *
     * @param activityId 活动 ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     *
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 签到返利接口
     *
     * @param userId 用户ID
     * @return 是否签到返利成功
     */
    Response<Boolean> calendarSignRebate(String userId);
}
