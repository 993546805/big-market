package com.tuto.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 基础事件
 *
 * @date 2024-10-31 23:27
 * @author tu
 */
@Data
public abstract class BaseEvent<T> {
    public abstract EventMessage<T> buildEventMessage(T t);

    public abstract String getTopic();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventMessage<T> {
        private String id;
        private Date timestamp;
        private T data;
    }
}
