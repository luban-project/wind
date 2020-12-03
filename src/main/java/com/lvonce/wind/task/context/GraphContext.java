package com.lvonce.wind.task.context;

import com.lvonce.wind.task.enums.BranchNode;
import com.lvonce.wind.task.enums.TaskSeq;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.RouteEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.route.ActionRoute;
import com.lvonce.wind.task.route.BranchRoute;
import com.lvonce.wind.task.route.Route;
import lombok.Data;

import java.util.*;

@Data
public class GraphContext {

    private String uuid = UUID.randomUUID().toString();
    /**
     * only one start task is allowed
     */
    private Integer start = null;
    private final Set<Integer> ends = new LinkedHashSet<>();
    private final Set<Integer> asyncTasks = new LinkedHashSet<>();
    private final Map<Integer, Integer> retryCountLimit = new LinkedHashMap<>();
    private final Map<Integer, Integer> nextTasks = new LinkedHashMap<>();
    private final Map<Integer, String> taskUuidMap = new LinkedHashMap<>();
    private final Map<Integer, Route> routeMap = new LinkedHashMap<>();

    private String bpmnDefinition = null;


    public void buildByDefinition(String definition) {
        this.bpmnDefinition = definition;
    }


    public <T> TaskRoutedEvent<T> route(int taskSeq, TaskEvent<T> event) {
        BranchNode node = event.getNode();
        TaskState state = event.getState();
        int nextTaskSeq = routeMap.get(taskSeq).getRoute(state, node);
        RouteEvent routeEvent = RouteEvent.of(taskSeq, nextTaskSeq);
        return TaskRoutedEvent.of(event, routeEvent);
    }

    public void registerTask(int taskSeq, String taskUuid) {
        taskUuidMap.put(taskSeq, taskUuid);
        retryCountLimit.put(taskSeq, 0);
    }

//    /**
//     * @param retryCountLimit value>0 means the count, value<0 means retry forever, value==0 means never
//     */
    public void registerTask(int taskSeq, String taskUuid, boolean isAsync, int retryCountLimit) {
        taskUuidMap.put(taskSeq, taskUuid);
        if (isAsync) {
            asyncTasks.add(taskSeq);
        }
        this.retryCountLimit.put(taskSeq, retryCountLimit);
    }


    public void addActionFlow(int taskSeq, int nextTaskSeq) {
        this.routeMap.put(taskSeq, ActionRoute.of(nextTaskSeq));
    }

    public void addBranchFlow(int taskSeq, BranchNode node, int nextTaskSeq) {
        Route route = this.routeMap.getOrDefault(taskSeq, new BranchRoute());
        route.setRoute(node, nextTaskSeq);
        this.routeMap.put(taskSeq, route);
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void addEnd(int end) {
        this.ends.add(end);
        addActionFlow(end, TaskSeq.END.getSeqValue());
    }

}
