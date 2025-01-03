package com.tuto.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.tuto.infrastructure.persistent.po.UserBehaviorRebateOrder;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tu
 * @date 2024-12-30 下午9:59
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

    @DBRouter(key = "userId")
    List<UserBehaviorRebateOrder> queryOrderByOutBusinessNo(UserBehaviorRebateOrder userBehaviorRebateOrderReq);
}
