package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.enums.TaskState;

public interface SimpleTask<R, I, E, T> extends ActionTask {

    @Override
    @SuppressWarnings("unchecked")
    default TaskEvent<?> inquire(TaskEvent<?> event) {
        return query((TaskEvent<R>) event);
    }

    @SuppressWarnings("unused")
    default TaskEvent<I> query(TaskEvent<R> event) {
        return TaskEvent.of(TaskState.NOT_SUPPORTED);
    }

    @Override
    @SuppressWarnings("unchecked")
    default TaskEvent<?> run(TaskEvent<?> event) {
        return execute((TaskEvent<E>) event);
    }

    @SuppressWarnings("unused")
    TaskEvent<T> execute(TaskEvent<E> event);
}
