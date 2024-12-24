package com.tuto.domain.task.model.entity;

import lombok.Data;

/**
 * 任务实体对象
 * @author tu
 * @date 2024-12-24 下午8:00
 */
@Data
public class TaskEntity {

    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private String message;

}
