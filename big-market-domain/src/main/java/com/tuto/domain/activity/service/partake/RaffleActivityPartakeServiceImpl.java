package com.tuto.domain.activity.service.partake;

import com.tuto.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.tuto.domain.activity.model.entity.*;
import com.tuto.domain.activity.model.valobj.UserRaffleOrderStateVO;
import com.tuto.domain.activity.repository.IActivityRepository;
import com.tuto.types.enums.ResponseCode;
import com.tuto.types.exception.AppException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tu
 * @date 2024-11-05 18:32
 */
@Service
public class RaffleActivityPartakeServiceImpl extends AbstractRaffleActivityPartakeService {

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM");
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");


    public RaffleActivityPartakeServiceImpl(IActivityRepository activityRepository) {
        super(activityRepository);
    }


    @Override
    protected CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate) {
        // 查询账户总额度
        ActivityAccountEntity activityAccountEntity = activityRepository.queryActivityAccountByUserId(userId, activityId);

        // 额度判断(只判断总剩余额度)
        if( null == activityAccountEntity || activityAccountEntity.getTotalCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACCOUNT_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_QUOTA_ERROR.getInfo());
        }

        String month = dateFormatMonth.format(currentDate);
        String day = dateFormatDay.format(currentDate);

        // 查询账户月额度
        ActivityAccountMonthEntity activityAccountMonthEntity = activityRepository.queryActivityAccountMonthByUserId(userId, activityId, month);
        if( null != activityAccountMonthEntity && activityAccountMonthEntity.getMonthCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_MONTH_QUOTA_ERROR.getInfo());
        }

        // 创建月账户额度；true = 存在月账户、false = 不存在月账户
        boolean isExistAccountMonth = null != activityAccountMonthEntity;
        if(null == activityAccountMonthEntity){
            activityAccountMonthEntity = ActivityAccountMonthEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .month(month)
                    .monthCount(activityAccountEntity.getMonthCount())
                    .monthCountSurplus(activityAccountEntity.getMonthCountSurplus())
                    .build();
        }

        // 查询账户日额度
        ActivityAccountDayEntity activityAccountDayEntity = activityRepository.queryActivityAccountDayByUserId(userId, activityId, day);
        if( null != activityAccountDayEntity && activityAccountDayEntity.getDayCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getCode(), ResponseCode.ACCOUNT_DAY_QUOTA_ERROR.getInfo());
        }

        // 创建日账户额度；true = 存在日账户、false = 不存在日账户
        boolean isExistAccountDay = null != activityAccountDayEntity;
        if(null == activityAccountDayEntity){
            activityAccountDayEntity = ActivityAccountDayEntity.builder()
                    .userId(userId)
                    .activityId(activityId)
                    .day(day)
                    .dayCount(activityAccountEntity.getDayCount())
                    .dayCountSurplus(activityAccountEntity.getDayCountSurplus())
                    .build();
        }

        // 构建对象
        CreatePartakeOrderAggregate createPartakeOrderAggregate = new CreatePartakeOrderAggregate();
        createPartakeOrderAggregate.setUserId(userId);
        createPartakeOrderAggregate.setActivityId(activityId);
        createPartakeOrderAggregate.setActivityAccountEntity(activityAccountEntity);
        createPartakeOrderAggregate.setExistAccountMonth(isExistAccountMonth);
        createPartakeOrderAggregate.setActivityAccountMonthEntity(activityAccountMonthEntity);
        createPartakeOrderAggregate.setExistAccountDay(isExistAccountDay);
        createPartakeOrderAggregate.setActivityAccountDayEntity(activityAccountDayEntity);

        return createPartakeOrderAggregate;
    }

    @Override
    protected UserRaffleOrderEntity buildUserRaffleOrder(String userId, Long activityId, Date currentDate) {
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);

        return UserRaffleOrderEntity.builder()
                .userId(userId)
                .activityId(activityId)
                .activityName(activityEntity.getActivityName())
                .strategyId(activityEntity.getStrategyId())
                .orderId(RandomStringUtils.randomNumeric(12))
                .orderTime(currentDate)
                .orderState(UserRaffleOrderStateVO.CREATE)
                .endDateTime(activityEntity.getEndDateTime())
                .build();
    }


}
