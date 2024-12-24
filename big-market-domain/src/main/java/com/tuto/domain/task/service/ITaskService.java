package com.tuto.domain.task.service;

import com.tuto.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * 任务接口
 *
 * @author tu
 * @date 2024-12-24 下午5:59
 */
public interface ITaskService {

    /**
     * 查询发送 MQ 失败和超时1分钟未发送的MQ
     *
     * @return 未发送的任务消息列表 10 条
     */
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity taskEntity);

    void updateTaskSendMessageCompleted(String userId, String messageId);

    void updateTaskSendMessageFail(String userId, String messageId);
}
