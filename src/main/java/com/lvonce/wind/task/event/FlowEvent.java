package com.lvonce.wind.task.event;

import com.lvonce.wind.task.enums.FlowState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FlowEvent<T> {

    final String id;
    final Timestamp time;
    final String source;
    final String subject;
    final String specversion;
    final String type;
    final String datacontenttype;
    final T data;
    final FlowState flowstate;

    public static <T> FlowEvent<T> of(FlowState flowState, T data) {
        return new FlowEvent<>(
                UUID.randomUUID().toString(),
                Timestamp.from(Instant.now()),
                "system",
                "system",
                "1.0",
                "system",
                "json",
                data,
                flowState
        );
    }

    public static <T> FlowEvent<T> of(FlowState state) {
        return of(state, null);
    }

    public static <T> FlowEvent<T> ofPending() {
        return of(FlowState.PENDING, null);
    }

    public static <T> FlowEvent<T> ofCanceling() {
        return of(FlowState.CANCELLING, null);
    }

    public static <T> FlowEvent<T> ofFail() {
        return of(FlowState.FAIL, null);
    }

    public static <T> FlowEvent<T> ofAbort() {
        return of(FlowState.ABORT, null);
    }
}
