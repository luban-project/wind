package task.saga;

import com.lvonce.wind.task.*;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.SagaTaskEngine;
import com.lvonce.wind.task.enums.BranchNode;
import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.enums.TaskSeq;
import com.lvonce.wind.task.enums.TaskState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.graph.SagaFlowGraph;
import com.lvonce.wind.task.tasks.BranchTask;
import com.lvonce.wind.task.tasks.ReducerTask;
import com.lvonce.wind.task.tasks.SagaTask;
import com.lvonce.wind.util.JsonUtil;
import lombok.Data;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import task.simple.BranchTaskTest;

import java.util.ArrayList;
import java.util.UUID;


/**
 *    input: a, b, sum
 *    1 ---> 2 ----> 3 ---> 4 ---> end
 *    a+b  a+b+1     |     sum * 2
 *                   |
 *                   ---> 5 (if sum < 0 fail else success)
 */

public class SagaFlowTest {

    @Data
    public static class TaskData {
        int a;
        int b;
        int sum;
    }

    @Getter
    public static class Task1
            implements SagaTask<Object, Object, TaskData, TaskData, TaskData, TaskData> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task1() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskData> execute(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(data);
            data.sum = event.getData().sum + event.getData().a + event.getData().b;
            return result;
        }


        @Override
        public TaskEvent<TaskData> cancel(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = event.getData().sum - event.getData().a - event.getData().b;
            return result;
        }
    }


    @Getter
    public static class Task2
            implements SagaTask<Object, Object, TaskData, TaskData, TaskData, TaskData> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task2() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }


        @Override
        public TaskEvent<TaskData> execute(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(data);
            data.sum = event.getData().sum + 1;
            return result;
        }

        @Override
        public TaskEvent<TaskData> cancel(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = event.getData().sum - 1;
            return result;
        }
    }

    @Getter
    public static class Task3
            implements BranchTask<TaskData> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task3() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.taskProperty.setBranch();
        }

        @Override
        public BranchNode branch(TaskEvent<TaskData> event) {
            TaskData inData = event.getData();
            if (inData.sum < 0) {
                return BranchNode.FAIL;
            }
            return BranchNode.ONE;
        }
    }

    @Getter
    public static class Task4
            implements SagaTask<Object, Object, TaskData, TaskData, TaskData, TaskData> {
        String taskUuid;
        TaskProperty taskProperty;

        public Task4() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<TaskData> execute(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(data);
            data.sum = event.getData().sum * 2;
            return result;
        }

        @Override
        public TaskEvent<TaskData> cancel(TaskEvent<TaskData> event) {
            TaskData data = event.getData();
            TaskEvent<TaskData> result = TaskEvent.of(TaskState.FAIL, data);
            data.sum = event.getData().sum / 2;
            return result;
        }
    }











    public void testCase(SagaFlowGraph graph, int a, int b, int sum, boolean isSuccess) {

        TaskData runEvent = new TaskData();
        runEvent.setA(a);
        runEvent.setB(b);
        runEvent.setSum(0);
        FlowEvent<?> result = graph.run(runEvent);

        System.out.println(JsonUtil.toJson(result).get());
        TaskData sumEvent = (TaskData) result.getData();
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

    @Getter
    public static class ResultTask implements ReducerTask<TaskData> {
        String taskUuid;
        TaskProperty taskProperty;
        int resultIdx;

        public ResultTask(int resultIdx) {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.resultIdx = resultIdx;
        }

        @Override
        public TaskEvent<TaskData> reduce(FlowContext context) {
            TaskData outEvent = context.getResultBySeq(resultIdx);
            TaskData flowResult = new TaskData();
            flowResult.a = outEvent.a;
            flowResult.b = outEvent.b;
            flowResult.sum = outEvent.sum;
            return TaskEvent.of(flowResult);
        }
    }

    @Test
    public void test() {

        Task1 task1 = new Task1();
        Task2 task2 = new Task2();
        Task3 task3 = new Task3();
        Task4 task4 = new Task4();
        ResultTask reducer = new ResultTask(4);

        SagaTaskEngine engine = new SagaTaskEngine();
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
        graphContext.addBranchFlow(3, BranchNode.ONE, 4);
        graphContext.addActionFlow(4, 5);
        graphContext.addEnd(5);


        SagaFlowGraph flowGraph = new SagaFlowGraph(graphContext, engine);


        testCase(flowGraph, 1, 2, 8, true);
        testCase(flowGraph, -2, 3, 4, true);
        testCase(flowGraph, 12, -13, 0, true);
        testCase(flowGraph, 11, -13, 0, false);
    }

}
