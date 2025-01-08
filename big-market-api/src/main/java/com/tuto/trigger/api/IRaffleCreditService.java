package com.tuto.trigger.api;

import com.tuto.trigger.api.dto.SubtractCreditRequestDTO;
import com.tuto.types.model.Response;

/**
 * 抽奖积分接口
 * @author tu
 * @date 2025-01-08 下午4:55
 */
public interface IRaffleCreditService {

    Response<Boolean> subtractCredit(SubtractCreditRequestDTO request);
}
