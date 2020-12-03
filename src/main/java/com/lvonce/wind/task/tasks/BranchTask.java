package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.enums.BranchNode;

public interface BranchTask<T> extends TccTask<T, T, T, T, T, T, T, T> {

    @Override
    @SuppressWarnings("unchecked")
    default TaskEvent<?> run(TaskEvent<?> event) {
        BranchNode node = branch((TaskEvent<T>)event);
        return TaskEvent.of(node, event.getData());
    }

    @Override
    @SuppressWarnings("unchecked")
    default TaskEvent<?> inquire(TaskEvent<?> event) {
        BranchNode node = branch((TaskEvent<T>) event);
        return TaskEvent.of(node, event.getData());
    }

    @Override
    default TaskEvent<T> query(TaskEvent<T> event) {
        BranchNode node = branch(event);
        return TaskEvent.of(node, event.getData());
    }

    @Override
    default TaskEvent<T> execute(TaskEvent<T> event) {
        BranchNode node = branch(event);
        return TaskEvent.of(node, event.getData());
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

    BranchNode branch(TaskEvent<T> event);
}
