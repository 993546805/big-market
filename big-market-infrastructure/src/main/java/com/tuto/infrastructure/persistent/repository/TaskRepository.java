package com.tuto.infrastructure.persistent.repository;

import com.tuto.domain.task.model.entity.TaskEntity;
import com.tuto.domain.task.repository.ITaskRepository;
import com.tuto.infrastructure.event.EventPublisher;
import com.tuto.infrastructure.persistent.dao.ITaskDao;
import com.tuto.infrastructure.persistent.po.Task;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tu
 * @date 2024-12-24 下午8:05
 */
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskDao.queryNoSendMessageTaskList().stream().map(item->{
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(item.getUserId());
            taskEntity.setTopic(item.getTopic());
            taskEntity.setMessageId(item.getMessageId());
            taskEntity.setMessage(item.getMessage());

            return taskEntity;
        }).collect(Collectors.toList());
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);

        taskDao.updateTaskSendMessageCompleted(task);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        Task task = new Task();
        task.setUserId(userId);
        task.setMessageId(messageId);

        taskDao.updateTaskSendMessageFail(task);
    }
}
