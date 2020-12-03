package com.lvonce.wind.task.engine;

import java.util.Map;
import java.util.LinkedHashMap;
import com.lvonce.wind.task.tasks.SagaTask;

public class SagaTaskEngine implements TaskEngine<SagaTask<?,?,?,?,?,?>> {

    private final Map<String, SagaTask<?, ?, ?, ?, ?, ?>> taskMap = new LinkedHashMap<>();

    public void register(SagaTask<?, ?, ?, ?, ?, ?> task) {
        this.taskMap.put(task.getTaskUuid(), task);
    }

    @Override
    public SagaTask<?,?,?,?,?,?> getTask(String taskUuid) {
        return this.taskMap.get(taskUuid);
    }

}
