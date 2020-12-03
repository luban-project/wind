package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.event.TaskEvent;

public interface Task {

    String getTaskUuid();

    TaskEvent<?> run(TaskEvent<?> event);

    default TaskEvent<?> run(FlowContext context, TaskEvent<?> event) {
        return run(event);
    }
}
