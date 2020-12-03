package com.lvonce.wind.task.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.enums.BranchNode;

@Data
@AllArgsConstructor
public class TaskEvent<T> {
    final BranchNode node;
    final TaskState state;
    final T data;

    public static <T> TaskEvent<T> of(T data) {
        return of(BranchNode.DEFAULT, TaskState.SUCCESS, data);
    }

    public static <T> TaskEvent<T> of(TaskState state) {
        return of(BranchNode.DEFAULT, state, null);
    }

    public static <T> TaskEvent<T> of(TaskState state, T data) {
        return of(BranchNode.DEFAULT, state, data);
    }

    public static <T> TaskEvent<T> of(BranchNode node, T data) {
        return of(node, TaskState.SUCCESS, data);
    }

    public static <T> TaskEvent<T> of(BranchNode node, TaskState state, T data) {
        return new TaskEvent<>(node, state, data);
    }
}
