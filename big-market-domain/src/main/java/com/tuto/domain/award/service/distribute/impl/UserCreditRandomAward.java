package com.tuto.domain.award.service.distribute.impl;

import com.tuto.domain.award.model.aggregate.GiveOutPrizesAggregate;
import com.tuto.domain.award.model.entity.DistributeAwardEntity;
import com.tuto.domain.award.model.entity.UserAwardRecordEntity;
import com.tuto.domain.award.model.entity.UserCreditAwardEntity;
import com.tuto.domain.award.model.valobj.AwardStateVO;
import com.tuto.domain.award.repository.IAwardRepository;
import com.tuto.domain.award.service.distribute.IDistributeAward;
import com.tuto.types.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

/**
 * @author tu
 * @date 2025-01-03 下午2:39
 */
@Component("user_credit_random")
public class UserCreditRandomAward implements IDistributeAward {

    @Resource
    private IAwardRepository repository;

    @Override
    public void giveOutPrizes(DistributeAwardEntity distributeAwardEntity) {
        // 奖品 ID
        Integer awardId = distributeAwardEntity.getAwardId();
        // 查询奖品 ID [优先走透传的随机积分奖品配置]
        String awardConfig = distributeAwardEntity.getAwardConfig();
        if (StringUtils.isBlank(awardConfig)) {
            awardConfig = repository.queryAwardConfig(awardId);
        }

        String[] creditRange = awardConfig.split(Constants.SPLIT);
        if (creditRange.length != 2){
            throw new RuntimeException("award_config [" + awardConfig + "] 配置不是一个范围值,如  1,100");
        }

        // 生成随机积分值
        BigDecimal creditAmount = generateRandom(new BigDecimal(creditRange[0]), new BigDecimal(creditRange[1]));


        // 构建聚合对象
        UserAwardRecordEntity userAwardRecordEntity = GiveOutPrizesAggregate.buildDistributeUserAwardRecordEntity(
                distributeAwardEntity.getUserId(),
                distributeAwardEntity.getOrderId(),
                distributeAwardEntity.getAwardId(),
                AwardStateVO.complete
        );

        UserCreditAwardEntity userCreditAwardEntity = UserCreditAwardEntity.builder()
                .userId(distributeAwardEntity.getUserId())
                .creditAmount(creditAmount)
                .build();

        GiveOutPrizesAggregate giveOutPrizesAggregate = GiveOutPrizesAggregate.builder()
                .userId(distributeAwardEntity.getUserId())
                .userAwardRecordEntity(userAwardRecordEntity)
                .userCreditAwardEntity(userCreditAwardEntity)
                .build();

        repository.saveGiveOutPrizesAggregate(giveOutPrizesAggregate);
    }

    private BigDecimal generateRandom(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.round(new MathContext(3));
    }
}
