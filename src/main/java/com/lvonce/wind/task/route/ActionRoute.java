package com.lvonce.wind.task.route;

import com.lvonce.wind.task.enums.TaskSeq;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.enums.BranchNode;

public class ActionRoute implements Route {

    int nextTaskSeq;

    @Override
    public void setRoute(BranchNode node, int taskSeq) {
        this.nextTaskSeq = taskSeq;
    }

    @Override
    public int getRoute(TaskState state, BranchNode node) {
        switch (state) {
            case PENDING:
            case CANCELLING:
            case CONFIRMING:
                return TaskSeq.RUNNING.getSeqValue();
            case NOT_SUPPORTED:
            case ABORT:
                return TaskSeq.ABORT.getSeqValue();
            case FAIL:
                return TaskSeq.FAIL.getSeqValue();
            default:
                return this.nextTaskSeq;
        }
    }

    public static ActionRoute of(int taskSeq) {
        ActionRoute route = new ActionRoute();
        route.nextTaskSeq = taskSeq;
        return route;
    }
}
