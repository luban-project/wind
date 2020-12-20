package com.lvonce.wind.task.context;

import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class FlowContext {

    /**
     * first event: currTaskSeq == nextTaskSeq == start
     * middle event: currTaskSeq(who provide the event), nextTaskSeq(who consume the event)
     * last event: currentTaskId(who produce the event), nextTaskSeq == -1
     */
    private final String uuid;
    private final ArrayList<TaskRoutedEvent<?>> path = new ArrayList<>();
    private final Map<Integer, TaskRoutedEvent<?>> resultMap = new LinkedHashMap<>();
    private FlowState flowState = FlowState.EMPTY;

    public FlowContext() {
        this.uuid = UUID.randomUUID().toString();
    }

    public FlowContext(String uuid) {
        this.uuid = uuid;
    }

    public TaskRoutedEvent<?> peekEvent() {
        int last = path.size() - 1;
        if (last < 0) {
            return null;
        }
        return path.get(last);
    }

    @SuppressWarnings("unchecked")
    public <T> T getResultBySeq(int taskSeq) {
        return (T) resultMap.get(taskSeq).getTaskevent().getData();
    }

    public void pushEvent(TaskRoutedEvent<?> event) {
        TaskState taskState = event.getTaskevent().getState();
        this.flowState = FlowState.from(taskState);
        this.path.add(event);
    }

    public void pushResult(TaskRoutedEvent<?> event) {
        this.resultMap.put(event.getRouteevent().getCurrTaskSeq(), event);
        this.pushEvent(event);
    }

    public void finish() {
        if (this.flowState.equals(FlowState.PENDING)) {
            this.flowState = FlowState.SUCCESS;
        }
        if (this.flowState.equals(FlowState.CANCELLING)) {
            this.flowState = FlowState.FAIL;
        }
    }

    @SuppressWarnings("unused")
    public TaskRoutedEvent<?> popEvent() {
        TaskRoutedEvent<?> result = null;
        int last = path.size() - 1;
        if (last >= 0) {
            result = path.remove(last);
        }
        return result;
    }
}
