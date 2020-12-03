package com.lvonce.wind.task.engine;

import java.util.Map;
import java.util.LinkedHashMap;
import com.lvonce.wind.task.tasks.TccTask;

public class TccTaskEngine implements TaskEngine<TccTask<?,?,?,?,?,?,?,?>> {

    private final Map<String, TccTask<?, ?, ?, ?, ?, ?, ?, ?>> taskMap = new LinkedHashMap<>();

    public void register(TccTask<?, ?, ?, ?, ?, ?, ?, ?> task) {
        this.taskMap.put(task.getTaskUuid(), task);
    }

    @Override
    public TccTask<?,?,?,?,?,?,?,?> getTask(String taskUuid) {
        return this.taskMap.get(taskUuid);
    }
}
