package com.lvonce.wind.task.graph;

import com.lvonce.wind.task.enums.FlowState;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

import com.lvonce.wind.task.tasks.TccTask;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.TccTaskEngine;
import com.lvonce.wind.task.event.TaskRoutedEvent;


@Getter
@AllArgsConstructor
public class TccFlowGraph<T, R> implements FlowGraph<T, R> {

    private final GraphContext graphContext;
    private final TccTaskEngine taskEngine;

    @Override
    public FlowEvent<R> cancel(FlowContext context) {
        return rollback(context);
    }

    @SuppressWarnings("unchecked")
    public FlowEvent<R> confirm(FlowContext context) {
        ArrayList<TaskRoutedEvent<?>> path = context.getPath();
        int size = context.getPath().size();
        for (int i = 0; i < size - 1; ++i) {
            // last event is the output event, so just ignore it

            // get the task
            TaskRoutedEvent<?> inEvent = path.get(i);
            int currTaskSeq = inEvent.getRouteevent().getNextTaskSeq();
            String taskUuid = this.graphContext.getTaskUuidMap().get(currTaskSeq);
            TccTask<?, ?, ?, ?, ?, ?, ?, ?> task = this.taskEngine.getTask(taskUuid);

            // commit the event
            TaskEvent<?> taskEvent = inEvent.getTaskevent();
            TaskEvent<?> confirmResult = task.commit(taskEvent);
            TaskState confirmState = confirmResult.getState();

            // confirm must be success, if not retry it later, return pending state
            if (!confirmState.equals(TaskState.SUCCESS)) {
                return FlowEvent.ofPending();
            }
        }
        R data = (R) context.peekEvent().getTaskevent().getData();
        return FlowEvent.of(FlowState.SUCCESS, data);
    }
}
