package task.tcc;

import com.lvonce.wind.task.*;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.TccTaskEngine;
import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.RouteEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.graph.TccFlowGraph;
import com.lvonce.wind.task.tasks.ReducerTask;
import com.lvonce.wind.task.tasks.TccTask;
import com.lvonce.wind.util.JsonUtil;
import lombok.Data;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import task.saga.SagaFlowTest;

import java.util.ArrayList;
import java.util.UUID;


/**
 *    input: sum
 *    1 ----> 2 ----> 3 ----> 4 ----> end
 *  sum-1   sum-2   sum-3   sum-4
 */

public class TccFlowTest {

    @Data
    public static class TaskExecuteEvent {
        int sum;
    }

    @Getter
    public static class Task1
            implements TccTask<Object, Object,
                        TaskExecuteEvent, TaskExecuteEvent,
                        TaskExecuteEvent, TaskExecuteEvent,
                        TaskExecuteEvent, TaskExecuteEvent> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task1() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskExecuteEvent> lock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(data);
            if (data.sum >= 1) {
                data.sum = data.sum - 1;
                System.out.println(String.format("[success] sum - 1 = %d", data.sum));
            } else {
                System.out.println(String.format("[fail] sum = %d", data.sum));
                return TaskEvent.of(TaskState.FAIL);
            }
            return result;
        }

        @Override
        public TaskEvent<TaskExecuteEvent> confirm(TaskEvent<TaskExecuteEvent> event) {
            return TaskEvent.of(TaskState.SUCCESS, null);
        }

        @Override
        public TaskEvent<TaskExecuteEvent> unlock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = data.sum + 1;
            System.out.println(String.format("[rollback] sum = %d", data.sum));
            return result;
        }
    }

    @Getter
    public static class Task2
            implements TccTask<Object, Object,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task2() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskExecuteEvent> lock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(data);
            if (data.sum >= 2) {
                data.sum = data.sum - 2;
                System.out.println(String.format("[success] sum - 2 = %d", data.sum));
            } else {
                System.out.println(String.format("[fail] sum = %d", data.sum));
                return TaskEvent.of(TaskState.FAIL);
            }
            return result;
        }

        @Override
        public TaskEvent<TaskExecuteEvent> confirm(TaskEvent<TaskExecuteEvent> event) {
            return TaskEvent.of(TaskState.SUCCESS, null);
        }

        @Override
        public TaskEvent<TaskExecuteEvent> unlock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = data.sum + 2;
            System.out.println(String.format("[rollback] sum = %d", data.sum));
            return result;
        }
    }

    @Getter
    public static class Task3
            implements TccTask<Object, Object,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task3() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskExecuteEvent> lock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(data);
            if (data.sum >= 3) {
                data.sum = data.sum - 3;
                System.out.println(String.format("[success] sum - 3 = %d", data.sum));
            } else {
                System.out.println(String.format("[fail] sum = %d", data.sum));
                return TaskEvent.of(TaskState.FAIL);
            }
            return result;
        }

        @Override
        public TaskEvent<TaskExecuteEvent> confirm(TaskEvent<TaskExecuteEvent> event) {
            return TaskEvent.of(TaskState.SUCCESS, null);
        }

        @Override
        public TaskEvent<TaskExecuteEvent> unlock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = data.sum + 3;
            System.out.println(String.format("[rollback] sum = %d", data.sum));
            return result;
        }
    }

    @Getter
    public static class Task4
            implements TccTask<Object, Object,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent,
            TaskExecuteEvent, TaskExecuteEvent> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task4() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskExecuteEvent> lock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(data);
            if (data.sum >= 4) {
                data.sum = data.sum - 4;
                System.out.println(String.format("[success] sum - 4 = %d", data.sum));
            } else {
                System.out.println(String.format("[fail] sum = %d", data.sum));
                return TaskEvent.of(TaskState.FAIL);
            }
            return result;
        }

        @Override
        public TaskEvent<TaskExecuteEvent> confirm(TaskEvent<TaskExecuteEvent> event) {
            return TaskEvent.of(TaskState.SUCCESS, null);
        }

        @Override
        public TaskEvent<TaskExecuteEvent> unlock(TaskEvent<TaskExecuteEvent> event) {
            TaskExecuteEvent data = event.getData();
            TaskEvent<TaskExecuteEvent> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = data.sum + 4;
            System.out.println(String.format("[rollback] sum = %d", data.sum));
            return result;
        }
    }






    @Getter
    public static class ResultTask implements ReducerTask<TaskExecuteEvent> {
        String taskUuid;
        TaskProperty taskProperty;
        int resultIdx;

        public ResultTask(int resultIdx) {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.resultIdx = resultIdx;
        }

        @Override
        public TaskEvent<TaskExecuteEvent> reduce(FlowContext context) {
            TaskExecuteEvent outEvent = context.getResultBySeq(resultIdx);
            TaskExecuteEvent flowResult = new TaskExecuteEvent();
            flowResult.sum = outEvent.sum;
            System.out.println(String.format("[reduce] sum = %d", flowResult.sum));
            return TaskEvent.of(flowResult);
        }
    }


    public void testCase(TccFlowGraph graph, int init, int sum, boolean isSuccess) {

        TaskExecuteEvent runEvent = new TaskExecuteEvent();
        runEvent.setSum(init);
        FlowEvent<?> result = graph.run(runEvent);

        System.out.println(JsonUtil.toJson(result).get());
        TaskExecuteEvent sumEvent = (TaskExecuteEvent) result.getData();
        if (isSuccess) {
            Assert.assertEquals(sum, sumEvent.sum);
        } else {
            Assert.assertEquals(result.getFlowstate(), FlowState.FAIL);
        }
    }

    @Data
    public static class FlowResult {
        int a;
        int b;
        int sum;
    }

    @Test
    public void test() {

        Task1 task1 = new Task1();
        Task2 task2 = new Task2();
        Task3 task3 = new Task3();
        Task4 task4 = new Task4();
        ResultTask reducer = new ResultTask(4);

        TccTaskEngine engine = new TccTaskEngine();
        engine.register(task1);
        engine.register(task2);
        engine.register(task3);
        engine.register(task4);
        engine.register(reducer);

        GraphContext graphContext = new GraphContext();
        graphContext.registerTask(1, task1.taskUuid);
        graphContext.registerTask(2, task2.taskUuid);
        graphContext.registerTask(3, task3.taskUuid);
        graphContext.registerTask(4, task4.taskUuid);
        graphContext.registerTask(5, reducer.taskUuid);

        graphContext.setStart(1);
        graphContext.addActionFlow(1, 2);
        graphContext.addActionFlow(2, 3);
        graphContext.addActionFlow(3, 4);
        graphContext.addActionFlow(4, 5);
        graphContext.addEnd(5);

        TccFlowGraph flowGraph = new TccFlowGraph(graphContext, engine);


        testCase(flowGraph, 12, 2, true);
        testCase(flowGraph, 13, 3, true);
        testCase(flowGraph, 7, 0, false);
    }

}
