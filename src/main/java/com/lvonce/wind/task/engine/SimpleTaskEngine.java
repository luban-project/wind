package com.lvonce.wind.task.engine;

import java.util.Map;
import java.util.LinkedHashMap;
import com.lvonce.wind.task.tasks.SimpleTask;

public class SimpleTaskEngine implements TaskEngine<SimpleTask<?,?,?,?>> {

    private final Map<String, SimpleTask<?, ?, ?, ?>> taskMap = new LinkedHashMap<>();

    public void register(SimpleTask<?, ?, ?, ?> task) {
        this.taskMap.put(task.getTaskUuid(), task);
    }

    @Override
    public SimpleTask<?,?,?,?> getTask(String taskUuid) {
        return this.taskMap.get(taskUuid);
    }

}
