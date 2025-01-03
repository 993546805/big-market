package com.tuto.domain.rebate;

import com.tuto.domain.award.model.valobj.TaskStateVO;
import com.tuto.domain.rebate.event.SendRebateMessageEvent;
import com.tuto.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import com.tuto.domain.rebate.model.entity.BehaviorEntity;
import com.tuto.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import com.tuto.domain.rebate.model.entity.TaskEntity;
import com.tuto.domain.rebate.model.valobj.DailyBehaviorRebateVO;
import com.tuto.domain.rebate.repository.IBehaviorRebateRepository;
import com.tuto.types.common.Constants;
import com.tuto.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author tu
 * @date 2024-12-26 下午5:35
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity entity) {
        // 1.查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(entity.getBehaviorTypeVO());

        // 2. 构建聚合对象
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        for (DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            // 拼装业务 ID; 用户 ID_返利类型_外部透传业务 ID
            String bizId = entity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + entity.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .userId(entity.getUserId())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .outBusinessNo(entity.getOutBusinessNo())
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());

            // MQ 消息对象
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(entity.getUserId())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .bizId(bizId)
                    .build();

            // 构建事件信息
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);

            // 组装任务对象
            TaskEntity task = new TaskEntity();
            task.setUserId(entity.getUserId());
            task.setTopic(sendRebateMessageEvent.getTopic());
            task.setMessageId(rebateMessageEventMessage.getId());
            task.setMessage(rebateMessageEventMessage);
            task.setState(TaskStateVO.create);

            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .userId(entity.getUserId())
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .task(task)
                    .build();

            behaviorRebateAggregates.add(behaviorRebateAggregate);
        }

        // 3.存储聚合对象数据
        behaviorRebateRepository.saveUserRebateRecord(entity.getUserId(), behaviorRebateAggregates);

        // 返回订单 ID 集合
        return orderIds;
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryOrderByOutBusinessNo(userId, outBusinessNo);

    }
}
