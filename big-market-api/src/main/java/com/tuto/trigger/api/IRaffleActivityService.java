package com.tuto.trigger.api;

import com.tuto.trigger.api.dto.*;
import com.tuto.types.model.Response;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 判断是否完成日历签到返利接口
     *
     * @param userId 用户ID
     * @return 是否完成日历签到返利接口
     */
    Response<Boolean> isCalendarSignRebate(String userId);

    /**
     * 查询用户参与活动信息
     *
     * @param request 用户活动账户请求对象
     * @return 用户活动账户响应对象
     */
    Response<UserActivityAccountResponseDTO> queryActivityAccount(UserActivityAccountRequestDTO request);

    /**
     * 积分兑换商品
     * @param request 积分兑换商品请求对象
     * @return 是否成功
     */
    Response<Boolean> creditPayExchangeSku(CreditPayExchangeSkuRequestDTO request);

    /**
     * 活动商品查询
     * @param activityId 活动 ID
     * @return 活动商品列表集合
     */
    Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId);

    /**
     * 查询用户积分
     * @param userId 用户 ID
     * @return 用户积分
     */
    Response<BigDecimal> queryUserCredit(String userId);
}
