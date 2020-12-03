package com.lvonce.wind.task.route;

import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.enums.BranchNode;

public interface Route {

    void setRoute(BranchNode node, int taskSeq);

    int getRoute(TaskState state, BranchNode node);
}
