package com.lvonce.wind.task.graph;

import lombok.Getter;
import lombok.AllArgsConstructor;

import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.SagaTaskEngine;

@Getter
@AllArgsConstructor
public class SagaFlowGraph<T, R> implements FlowGraph<T, R> {

    private final GraphContext graphContext;
    private final SagaTaskEngine taskEngine;

    @Override
    public FlowEvent<R> cancel(FlowContext context) {
        return rollback(context);
    }

}
