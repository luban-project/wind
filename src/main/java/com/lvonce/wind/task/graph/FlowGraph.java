package com.lvonce.wind.task.graph;

import com.lvonce.wind.task.context.ContextSaver;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.TaskEngine;
import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.enums.TaskSeq;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.RouteEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.tasks.SagaTask;
import com.lvonce.wind.task.tasks.Task;

public interface FlowGraph<T, R> {

    GraphContext getGraphContext();

    TaskEngine<?> getTaskEngine();

    default ContextSaver getContextSaver() { return new ContextSaver.EmptyContextSaver(); }

    @SuppressWarnings("unchecked")
    default FlowEvent<R> confirm(FlowContext context) {

        R data = (R) context.peekEvent().getTaskevent().getData();
        return FlowEvent.of(FlowState.SUCCESS, data);
    }

    @SuppressWarnings("unused")
    default FlowEvent<R> cancel(FlowContext context) {
        return FlowEvent.ofFail();
    }


    default FlowEvent<R> run(T dataEvent) {
        int startTask = getGraphContext().getStart();
        RouteEvent routeEvent = RouteEvent.of(startTask, startTask);

        TaskEvent<T> taskEvent = TaskEvent.of(dataEvent);
        TaskRoutedEvent<T> inEvent = TaskRoutedEvent.of(taskEvent, routeEvent);

        FlowContext context = new FlowContext();
        context.pushEvent(inEvent);
        return execute(context);
    }

    default FlowEvent<R> rollback(FlowContext context) {
        while (!context.getPath().isEmpty()) {
            TaskRoutedEvent<?> inEvent = context.peekEvent();
            int currTaskSeq = inEvent.getRouteevent().getNextTaskSeq();
            String taskUuid = getGraphContext().getTaskUuidMap().get(currTaskSeq);
            SagaTask<?, ?, ?, ?, ?, ?> task = (SagaTask<?, ?, ?, ?, ?, ?>) getTaskEngine().getTask(taskUuid);
            TaskEvent<?> taskEvent = inEvent.getTaskevent();
            TaskEvent<?> cancelResult = task.rollback(taskEvent);
            TaskState cancelState = cancelResult.getState();

            // cancel must reach fail, if not retry it later, return cancelling state
            if (!cancelState.equals(TaskState.FAIL)) {
                return FlowEvent.ofFail();
            } else {
                context.popEvent();
            }
        }
        return FlowEvent.ofFail();
    }

    default FlowEvent<R> execute(FlowContext context) {
        TaskRoutedEvent<?> currentEvent = context.peekEvent();
        GraphContext graphContext = getGraphContext();
        while (true) {
            RouteEvent flowEvent = currentEvent.getRouteevent();
            int currTaskSeq = flowEvent.getNextTaskSeq();
            String taskUuid = graphContext.getTaskUuidMap().get(currTaskSeq);
            Task task = getTaskEngine().getTask(taskUuid);
            TaskEvent<?> inEvent = currentEvent.getTaskevent();
            TaskEvent<?> outEvent = task.run(context, inEvent);

            // if task is not finished, just return pending or other unfinished state
            if (!outEvent.getState().isFinish()) {
                // do not save to context, just return
                return FlowEvent.ofPending();
            }

            // route the task, decide the next task seq
            TaskRoutedEvent<?> resultEvent = graphContext.route(currTaskSeq, outEvent);
            int nextTaskSeq = resultEvent.getRouteevent().getNextTaskSeq();

            if (TaskSeq.isAbort(nextTaskSeq)) {
                return FlowEvent.ofAbort();
            }

            if (TaskSeq.isFail(nextTaskSeq)) {
                // because the last event as input event, is already failed or cancelled, so pop the last one
                context.popEvent();
                return cancel(context);
            }

//            getContextSaver().
            context.pushResult(resultEvent);
            currentEvent = resultEvent;

            // if this task is an end task, return final result
            if (TaskSeq.isEnd(nextTaskSeq)) {
                context.finish();
                return confirm(context);
            }
        }
    }

}
