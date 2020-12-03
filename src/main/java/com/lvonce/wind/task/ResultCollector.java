package com.lvonce.wind.task;

import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.event.FlowEvent;

public interface ResultCollector<T> {
    FlowEvent<T> collect(FlowContext context);
}
