package com.lvonce.wind.task.context;

import com.lvonce.wind.task.event.TaskRoutedEvent;

public interface ContextSaver {
    void save(FlowContext context);

    void saveStep(FlowContext context, TaskRoutedEvent<?> event);

    class EmptyContextSaver implements ContextSaver {
        @Override
        public void save(FlowContext context) {
        }

        @Override
        public void saveStep(FlowContext context, TaskRoutedEvent<?> event) {
        }
    }
}
