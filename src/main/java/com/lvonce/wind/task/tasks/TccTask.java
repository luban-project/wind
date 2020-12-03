package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.enums.TaskState;

public interface TccTask<A, B, C, D, E, F, G, H> extends SagaTask<A,B,C,D,E,F> {

    @Override
    @SuppressWarnings("unchecked")
    default TaskEvent<?> run(TaskEvent<?> event) {
        return lock((TaskEvent<C>)event);
    }

    @SuppressWarnings("unchecked")
    default TaskEvent<?> commit(TaskEvent<?> event) {
        return confirm((TaskEvent<E>)event);
    }

    @SuppressWarnings("unchecked")
    default TaskEvent<?> rollback(TaskEvent<?> event) {
        return unlock((TaskEvent<G>)event);
    }

    default TaskEvent<D> execute(TaskEvent<C> event) { return TaskEvent.of(TaskState.NOT_SUPPORTED);}

    @SuppressWarnings("unused")
    default TaskEvent<D> lock(TaskEvent<C> event) {
        return TaskEvent.of(TaskState.NOT_SUPPORTED);
    }

    @SuppressWarnings("unused")
    default TaskEvent<F> confirm(TaskEvent<E> event) {
        return TaskEvent.of(TaskState.NOT_SUPPORTED);
    }

    @SuppressWarnings("unused")
    default TaskEvent<H> unlock(TaskEvent<G> event) {
        return TaskEvent.of(TaskState.NOT_SUPPORTED);
    }
}
