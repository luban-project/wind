package com.lvonce.wind.task.tasks;


import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.TaskEvent;

public interface SagaTask<A, B, C, D, E, F> extends SimpleTask<A, B, C, D> {

    @SuppressWarnings("unchecked")
    default TaskEvent<?> rollback(TaskEvent<?> event) {
        return cancel((TaskEvent<E>)event);
    }

    @SuppressWarnings("unused")
    default TaskEvent<F> cancel(TaskEvent<E> event) {
        return TaskEvent.of(TaskState.NOT_SUPPORTED);
    }
}
