package task.simple;

import com.lvonce.wind.task.*;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.SimpleTaskEngine;
import com.lvonce.wind.task.enums.BranchNode;
import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.graph.SimpleFlowGraph;
import com.lvonce.wind.task.tasks.BranchTask;
import com.lvonce.wind.task.tasks.ReducerTask;
import com.lvonce.wind.task.tasks.SimpleTask;
import com.lvonce.wind.util.JsonUtil;
import lombok.Data;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class BranchTaskTest {

    @Data
    public static class SimpleRunEvent {
        private int a;
        private int b;
        private int sum;
    }


    @Data
    public static class Task1
            implements BranchTask<SimpleRunEvent> {
        String taskUuid;
        TaskProperty taskProperty;
        Map<Integer, Integer> routeMap;

        public Task1() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.taskProperty.setBranch();
        }

        @Override
        public BranchNode branch(TaskEvent<SimpleRunEvent> event) {
            SimpleRunEvent data = event.getData();
            if (data.a < 0 && data.b < 0) {
                return BranchNode.FAIL;
            }
            if (data.a < 0 || data.b < 0) {
                return BranchNode.ONE;
            } else {
                return BranchNode.TWO;
            }
        }
    }

    @Getter
    public static class Task2 implements SimpleTask
            <SimpleRunEvent, SimpleRunEvent, SimpleRunEvent, SimpleRunEvent> {

        String taskUuid;
        TaskProperty taskProperty;

        public Task2() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<SimpleRunEvent> execute(TaskEvent<SimpleRunEvent> event) {
            SimpleRunEvent data = new SimpleRunEvent();
            data.sum = event.getData().a + event.getData().b;
            return TaskEvent.of(data);
        }
    }

    @Getter
    public static class Task3 implements SimpleTask
            <SimpleRunEvent, SimpleRunEvent, SimpleRunEvent, SimpleRunEvent> {

        String taskUuid;
        TaskProperty taskProperty;

        public Task3() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<SimpleRunEvent> execute(TaskEvent<SimpleRunEvent> event) {
            SimpleRunEvent data = new SimpleRunEvent();
            data.sum = event.getData().a * event.getData().b;
            return TaskEvent.of(data);
        }
    }




    public void testCase(SimpleFlowGraph graph, int a, int b, int sum, boolean isSuccess) {

        SimpleRunEvent runEvent = new SimpleRunEvent();
        runEvent.setA(a);
        runEvent.setB(b);
        FlowEvent<?> result = graph.run(runEvent);

        System.out.println(JsonUtil.toJson(result).get());
        FlowResult sumEvent = (FlowResult) result.getData();
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
    public static class ResultTask implements ReducerTask<FlowResult> {
        String taskUuid;
        TaskProperty taskProperty;
        int resultIdx;

        public ResultTask(int resultIdx) {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.resultIdx = resultIdx;
        }

        @Override
        public TaskEvent<FlowResult> reduce(FlowContext context) {
            SimpleRunEvent outEvent = context.getResultBySeq(resultIdx);
            FlowResult flowResult = new FlowResult();
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
        ResultTask reducer2 = new ResultTask(2);
        ResultTask reducer3 = new ResultTask(3);

        SimpleTaskEngine engine = new SimpleTaskEngine();
        engine.register(task1);
        engine.register(task2);
        engine.register(task3);
        engine.register(reducer2);
        engine.register(reducer3);

        GraphContext graphContext = new GraphContext();
        graphContext.registerTask(1, task1.taskUuid);
        graphContext.registerTask(2, task2.taskUuid);
        graphContext.registerTask(3, task3.taskUuid);
        graphContext.registerTask(4, reducer2.taskUuid);
        graphContext.registerTask(5, reducer3.taskUuid);

        graphContext.setStart(1);
        graphContext.addBranchFlow(1, BranchNode.ONE, 2);
        graphContext.addBranchFlow(1, BranchNode.TWO, 3);
        graphContext.addActionFlow(2, 4);
        graphContext.addActionFlow(3, 5);
        graphContext.addEnd(4);
        graphContext.addEnd(5);


        SimpleFlowGraph flowGraph = new SimpleFlowGraph(graphContext, engine);


        testCase(flowGraph, 1, 2, 2, true);
        testCase(flowGraph, -2, 3, 1, true);
        testCase(flowGraph, 12, 12, 144, true);
        testCase(flowGraph, -12, -12, 0, false);
    }

}
