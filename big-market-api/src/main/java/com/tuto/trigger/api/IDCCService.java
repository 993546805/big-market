package com.tuto.trigger.api;

import com.tuto.types.model.Response;

/**
 * DCC 动态配置中心
 * @author tu
 * @date 2025-02-17 下午5:33
 */
public interface IDCCService {

    String BASE_CONFIG_PATH = "/big-market-dcc";
    String BASE_CONFIG_PATH_CONFIG = BASE_CONFIG_PATH + "/config";

    Response<Boolean> updateConfig(String key, String value);
}
