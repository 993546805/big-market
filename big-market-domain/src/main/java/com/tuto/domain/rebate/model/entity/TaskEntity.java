package com.tuto.domain.rebate.model.entity;

import com.tuto.domain.award.model.valobj.TaskStateVO;
import com.tuto.domain.rebate.event.SendRebateMessageEvent;
import com.tuto.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务实体对象
 * @author tu
 * @date 2024-12-30 下午9:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    /** 用户  ID*/
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主题 */
    private BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> message;
    /** 任务状态: create-创建 completed-完成 fail-失败 */
    private TaskStateVO state;
}
