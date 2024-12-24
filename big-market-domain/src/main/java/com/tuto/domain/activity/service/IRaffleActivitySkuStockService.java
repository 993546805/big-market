package com.tuto.domain.activity.service;

import com.tuto.domain.activity.model.valobj.ActivitySkuStockKeyVO;


public interface IRaffleActivitySkuStockService {

    /**
     * 获取活动 sku库存消耗队列
     * @return
     * @throws InterruptedException
     */
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 缓存库存已消耗完毕,清空数据库库存
     */
    void clearActivitySkuStock(Long sku);

    /**
     * 延迟队列 + 任务趋势更新活动 sku 库存
     */
    void updateActivitySkuStock(Long sku);

}
