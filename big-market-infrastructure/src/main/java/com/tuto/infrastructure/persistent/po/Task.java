package com.tuto.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author tu
 * @date 2024-11-01 16:27
 */
@Data
public class Task {
    /** 自增 ID */
    private Long id;
    /** 用户 ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    private String messageId;
    /** 消息内容 */
    private String message;
    /** 任务状态 create-创建 completed-完成 fail-失败 */
    private String state;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
