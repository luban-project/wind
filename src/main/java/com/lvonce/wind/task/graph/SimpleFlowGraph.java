package com.lvonce.wind.task.graph;

import lombok.Getter;
import lombok.AllArgsConstructor;

import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.SimpleTaskEngine;

@Getter
@AllArgsConstructor
public class SimpleFlowGraph<T, R> implements FlowGraph<T, R> {
    private final GraphContext graphContext;
    private final SimpleTaskEngine taskEngine;
}
