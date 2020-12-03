package com.lvonce.wind.task.context;

public interface ContextSaver {
    void save(FlowContext context);

    void saveStep(FlowContext context);

    class EmptyContextSaver implements ContextSaver {
        @Override
        public void save(FlowContext context) {
        }

        @Override
        public void saveStep(FlowContext context) {
        }
    }
}
