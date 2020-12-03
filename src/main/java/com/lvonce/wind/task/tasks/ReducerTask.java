package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.context.FlowContext;

public interface ReducerTask<T> extends TccTask<T, T, T, T, T, T, T, T> {

    @Override
    default TaskEvent<?> run(TaskEvent<?> event) {
        return event;
    }

    @Override
    default TaskEvent<?> inquire(TaskEvent<?> event) {
        return event;
    }

    @Override
    default TaskEvent<T> query(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<T> execute(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<?> commit(TaskEvent<?> event) {
        return event;
    }

    @Override
    default TaskEvent<?> rollback(TaskEvent<?> event) {
        return event;
    }

    @Override
    default TaskEvent<T> lock(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<T> confirm(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<T> unlock(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<T> cancel(TaskEvent<T> event) {
        return event;
    }

    @Override
    default TaskEvent<?> run(FlowContext context, TaskEvent<?> event) {
        return reduce(context);
    }

    TaskEvent<T> reduce(FlowContext context);
}
