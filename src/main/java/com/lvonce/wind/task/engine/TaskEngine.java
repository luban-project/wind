package com.lvonce.wind.task.engine;

import com.lvonce.wind.task.tasks.Task;

public interface TaskEngine<T extends Task> {
    T getTask(String taskUuid);
}
