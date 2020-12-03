package com.lvonce.wind.task.tasks;

import com.lvonce.wind.task.TaskProperty;
import com.lvonce.wind.task.event.TaskEvent;

public interface ActionTask extends Task {

    TaskProperty getTaskProperty();

    TaskEvent<?> inquire(TaskEvent<?> event);
}
