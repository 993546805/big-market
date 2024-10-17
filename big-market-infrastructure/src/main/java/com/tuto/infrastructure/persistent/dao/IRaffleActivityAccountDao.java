package com.tuto.infrastructure.persistent.dao;

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

}
