package com.tuto.infrastructure.persistent.dao;

import com.tuto.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tu
 * @date 2024-10-17 11:24
 */
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);
}
