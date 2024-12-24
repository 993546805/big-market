package com.tuto.trigger.job;

import com.tuto.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.tuto.domain.activity.service.IRaffleActivitySkuStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-11-01 00:29
 */
@Slf4j
@Component
public class UpdateActivitySkuStockJob {

    @Resource
    private IRaffleActivitySkuStockService skuStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("定时任务,更新活动sku 库存[延时队列获取,降低对数据库的更新频次,不要产生竞争");
            ActivitySkuStockKeyVO activitySkuStockKeyVO = skuStock.takeQueueValue();
            if (null == activitySkuStockKeyVO)
                return;
            log.info("定时任务,更新活动 sku 库存 sku:{} activityId:{}", activitySkuStockKeyVO.getSku(), activitySkuStockKeyVO.getActivityId());
            skuStock.updateActivitySkuStock(activitySkuStockKeyVO.getSku());

        } catch (Exception e) {
            log.error("定时任务,更新活动 sku 库存异常", e);
        }
    }
}
