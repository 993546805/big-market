package com.tuto.trigger.http;

import com.alibaba.fastjson.JSON;
import com.tuto.domain.activity.model.entity.ActivityAccountEntity;
import com.tuto.domain.activity.model.entity.UserRaffleOrderEntity;
import com.tuto.domain.activity.service.IRaffleActivityAccountQuotaService;
import com.tuto.domain.activity.service.IRaffleActivityPartakeService;
import com.tuto.domain.activity.service.armory.IActivityArmory;
import com.tuto.domain.activity.service.quota.RaffleActivityAccountQuotaService;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import com.tuto.domain.award.model.valobj.AwardStateVO;
import com.tuto.domain.award.service.IAwardService;
import com.tuto.domain.rebate.IBehaviorRebateService;
import com.tuto.domain.rebate.model.entity.BehaviorEntity;
import com.tuto.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.tuto.domain.rebate.model.valobj.BehaviorTypeVO;
import com.tuto.domain.strategy.model.entity.RaffleAwardEntity;
import com.tuto.domain.strategy.model.entity.RaffleFactorEntity;
import com.tuto.domain.strategy.service.IRaffleStrategy;
import com.tuto.domain.strategy.service.armory.IStrategyArmory;
import com.tuto.trigger.api.IRaffleActivityService;
import com.tuto.trigger.api.dto.ActivityDrawRequestDTO;
import com.tuto.trigger.api.dto.ActivityDrawResponseDTO;
import com.tuto.trigger.api.dto.UserActivityAccountRequestDTO;
import com.tuto.trigger.api.dto.UserActivityAccountResponseDTO;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import com.tuto.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 抽奖活动服务,在不引用 applications/case层时,需要让接口实现层来做领域的串联,一些较大规模的系统,需要加入 case 层
 *
 * @author tu
 * @date 2024-12-24 下午8:36
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {

    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyyMMdd");

    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleStrategy raffleStrategy;
    @Resource
    private IAwardService awardService;
    @Resource
    private IActivityArmory activityArmory;
    @Resource
    private IStrategyArmory strategyArmory;
    @Resource
    private IBehaviorRebateService behaviorRebateService;
    @Autowired
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    /**
     * 活动装配 - 数据预热 | 把活动配置的对应的 sku 一起装配
     *
     * @param activityId 活动ID
     * @return 装配结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/armory">/api/v1/raffle/activity/armory</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * <p>
     * curl --request GET \
     * --url 'http://localhost:8091/api/v1/raffle/activity/armory?activityId=100301'
     */
    @RequestMapping(value = "armory", method = RequestMethod.GET)
    @Override
    public Response<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("活动装配,数据预热,开始 activityId: {}", activityId);
            // 1.活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);
            // 2.策略装配
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(true).build();
            log.info("活动装配,数据预热,完成 activityId: {}", activityId);
            return response;
        } catch (Exception e) {
            log.error("活动装配,数据预热,失败 activityId: {}", activityId, e);
            return Response.<Boolean>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }

    /**
     * 抽奖接口
     *
     * @param request 请求对象
     * @return 抽奖结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/draw">/api/v1/raffle/activity/draw</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     * <p>
     * curl --request POST \
     * --url http://localhost:8091/api/v1/raffle/activity/draw \
     * --header 'content-type: application/json' \
     * --data '{
     * "userId":"xiaofuge",
     * "activityId": 100301
     * }'
     */
    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("活动抽奖 userId: {} activityId: {}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2.参与活动 - 创建参与记录订单
            UserRaffleOrderEntity orderEntity = raffleActivityPartakeService.createOrder(request.getUserId(), request.getActivityId());
            log.info("活动抽奖，创建订单 userId:{} activityId:{} orderId:{}", request.getUserId(), request.getActivityId(), orderEntity.getOrderId());
            // 3.抽奖策略 - 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder().userId(orderEntity.getUserId()).strategyId(orderEntity.getStrategyId()).endDateTime(orderEntity.getEndDateTime()).build());
            // 4.存放结果 - 写入中奖记录
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder().userId(orderEntity.getUserId()).activityId(orderEntity.getActivityId()).strategyId(orderEntity.getStrategyId()).orderId(orderEntity.getOrderId()).awardId(raffleAwardEntity.getAwardId()).awardTitle(raffleAwardEntity.getAwardTitle()).awardTime(new Date()).awardStateVO(AwardStateVO.create).build();
            awardService.saveUserAwardRecord(userAwardRecord);
            // 5.返回结果
            return Response.<ActivityDrawResponseDTO>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(ActivityDrawResponseDTO.builder().awardId(userAwardRecord.getAwardId()).awardTitle(userAwardRecord.getAwardTitle()).awardIndex(raffleAwardEntity.getSort()).build()).build();
        } catch (AppException e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder().code(e.getCode()).info(e.getInfo()).build();
        } catch (Exception e) {
            log.error("活动抽奖失败 userId:{} activityId:{}", request.getUserId(), request.getActivityId(), e);
            return Response.<ActivityDrawResponseDTO>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }

    @RequestMapping(value = "calendar_sign_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> calendarSignRebate(String userId) {
        try {
            log.info("日历签到返奖 userId: {}", userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SING);
            behaviorEntity.setOutBusinessNo(dateFormatDay.format(new Date()));
            List<String> orderIds = behaviorRebateService.createOrder(behaviorEntity);
            log.info("日历签到返利完成 userId: {} orderIds: {}", userId, JSON.toJSONString(orderIds));

            return Response.<Boolean>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).build();
        } catch (AppException e) {
            log.error("日历签到返利异常 userId: {}", userId, e);
            return Response.<Boolean>builder().code(e.getCode()).info(e.getInfo()).build();
        } catch (Exception e) {
            log.error("日历签到返利失败 userId: {}", userId, e);
            return Response.<Boolean>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }

    /**
     * 查询用户是否完成日历签到返利
     * @param userId 用户ID
     * @return 是否完成日历签到返利
     */
    @RequestMapping(value = "is_calendar_sign_rebate",method = RequestMethod.POST)
    @Override
    public Response<Boolean> isCalendarSignRebate(@RequestParam String userId) {
        try {
            log.info("查询用户是否完成日历签到返利开始 userId: {}", userId);
            String outBusinessNo = dateFormatDay.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = behaviorRebateService.queryOrderByOutBusinessNo(userId, outBusinessNo);
            log.info("查询用户是否完成日历签到返利完成 userId: {} orderSize: {}", userId, behaviorRebateOrderEntities.size());
            return Response.<Boolean>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(!behaviorRebateOrderEntities.isEmpty()).build();

        } catch (Exception e) {
            log.error("查询用户是否完成日历签到返利失败 userId: {}", userId, e);
            return Response.<Boolean>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }

    /**
     * 查询用户活动账户额度
     * @param request 用户活动账户请求对象
     * @return 用户活动账户
     */
    @RequestMapping(value = "query_user_activity_account", method = RequestMethod.POST)
    @Override
    public Response<UserActivityAccountResponseDTO> queryActivityAccount(UserActivityAccountRequestDTO request) {
        try {
            log.info("查询用户活动账户开始 userId: {} activityId: {}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2. 查询
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryActivityAccountEntity(request.getActivityId(), request.getUserId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = UserActivityAccountResponseDTO.builder()
                    .totalCount(activityAccountEntity.getTotalCount())
                    .totalCountSurplus(activityAccountEntity.getTotalCountSurplus())
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .build();
            log.info("查询用户活动账户完成 userId: {} activityId: {} dto: {}", request.getUserId(), request.getActivityId(), JSON.toJSONString(userActivityAccountResponseDTO));
            return Response.<UserActivityAccountResponseDTO>builder().code(ResponseCode.SUCCESS.getCode()).info(ResponseCode.SUCCESS.getInfo()).data(userActivityAccountResponseDTO).build();
        } catch (Exception e) {
            log.error("查询用户活动账户额度失败 userId: {} activityId: {}", request.getUserId(), request.getActivityId(), e);
            return Response.<UserActivityAccountResponseDTO>builder().code(ResponseCode.UN_ERROR.getCode()).info(ResponseCode.UN_ERROR.getInfo()).build();
        }
    }
}
