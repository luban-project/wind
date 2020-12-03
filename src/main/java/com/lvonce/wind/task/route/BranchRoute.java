package com.lvonce.wind.task.route;

import com.lvonce.wind.task.enums.BranchNode;
import com.lvonce.wind.task.enums.TaskSeq;
import com.lvonce.wind.task.enums.TaskState;

public class BranchRoute implements Route {

    final int[] routes;

    public BranchRoute() {
        this.routes = new int[BranchNode.MAX.val];
        this.routes[BranchNode.END.val] = TaskSeq.END.getSeqValue();
        this.routes[BranchNode.FAIL.val] = TaskSeq.FAIL.getSeqValue();
        this.routes[BranchNode.ABORT.val] = TaskSeq.ABORT.getSeqValue();
    }

    @Override
    public void setRoute(BranchNode node, int taskSeq) {
        this.routes[node.val] = taskSeq;
    }

    @Override
    public int getRoute(TaskState state, BranchNode node) {
        return this.routes[node.val];
    }
}
