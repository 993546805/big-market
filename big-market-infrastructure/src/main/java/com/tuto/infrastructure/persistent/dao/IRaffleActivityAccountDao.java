package com.tuto.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.tuto.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tu
 * @date 2024-10-17 11:23
 */
@Mapper
public interface IRaffleActivityAccountDao {

    void insert(RaffleActivityAccount raffleActivityAccount);

    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    int updateActivityAccountSubtractionQuota(RaffleActivityAccount raffleActivityAccount);

    int updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount raffleActivityAccount);

    int updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount raffleActivityAccount);

    @DBRouter
    RaffleActivityAccount queryActivityAccountByUserId(RaffleActivityAccount raffleActivityAccount);

    void updateActivityAccountMonthSubtractionQuota(RaffleActivityAccount raffleActivityAccount);

    void updateActivityAccountDaySubtractionQuota(RaffleActivityAccount raffleActivityAccount);
}
