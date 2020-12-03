package task.simple;

import com.lvonce.wind.task.*;
import com.lvonce.wind.task.context.FlowContext;
import com.lvonce.wind.task.context.GraphContext;
import com.lvonce.wind.task.engine.SimpleTaskEngine;
import com.lvonce.wind.task.enums.FlowState;
import com.lvonce.wind.task.event.FlowEvent;
import com.lvonce.wind.task.event.TaskEvent;
import com.lvonce.wind.task.event.TaskRoutedEvent;
import com.lvonce.wind.task.graph.SimpleFlowGraph;
import com.lvonce.wind.task.tasks.ReducerTask;
import com.lvonce.wind.task.tasks.SimpleTask;
import com.lvonce.wind.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SumTaskTest {

    @Data
    @AllArgsConstructor
    public static class SimpleSumEvent {
        private int a;
        private int b;
        private int sum;
    }


    @Getter
    public static class SumTask implements SimpleTask
            <SimpleSumEvent, SimpleSumEvent, SimpleSumEvent, SimpleSumEvent> {

        String taskUuid;
        TaskProperty taskProperty;

        public SumTask() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
            this.taskProperty.setIdempotent();
        }


        @Override
        public TaskEvent<SimpleSumEvent> execute(TaskEvent<SimpleSumEvent> event) {
            SimpleSumEvent data = new SimpleSumEvent(0, 0, 0);
            SimpleSumEvent inEvent = event.getData();
            data.sum = inEvent.a + inEvent.b;
            return TaskEvent.of(data);
        }
    }

    @Getter
    public static class ResultTask implements ReducerTask<FlowResult> {
        String taskUuid;
        TaskProperty taskProperty;

        public ResultTask() {
            this.taskUuid = UUID.randomUUID().toString();
            this.taskProperty = new TaskProperty();
        }

        @Override
        public TaskEvent<FlowResult> reduce(FlowContext context) {
            SimpleSumEvent outEvent = context.getResultBySeq(1);
            FlowResult flowResult = new FlowResult();
            flowResult.a = outEvent.a;
            flowResult.b = outEvent.b;
            flowResult.sum = outEvent.sum;
            return TaskEvent.of(flowResult);
        }

    }

    public void testCase(SimpleFlowGraph<SimpleSumEvent, SimpleSumEvent> graph, int a, int b, int sum) {
        SimpleSumEvent runEvent = new SimpleSumEvent(a, b, 0);
        FlowEvent<?> result = graph.run(runEvent);
        System.out.println(JsonUtil.toJson(result));
        FlowResult flowResult = (FlowResult) result.getData();
        Assert.assertEquals(sum, flowResult.sum);
    }

    @Data
    public static class FlowResult {
        int a;
        int b;
        int sum;
    }

    @Test
    public void test() {
        SumTask task = new SumTask();
        ResultTask reducer = new ResultTask();

        SimpleTaskEngine engine = new SimpleTaskEngine();
        engine.register(task);
        engine.register(reducer);

        GraphContext context = new GraphContext();
        context.registerTask(1, task.taskUuid);
        context.registerTask(2, reducer.taskUuid);

        context.setStart(1);
        context.addActionFlow(1,2 );
        context.addEnd(2);

        SimpleFlowGraph<SimpleSumEvent, SimpleSumEvent> flowGraph = new SimpleFlowGraph(context, engine);



        testCase(flowGraph, 1, 2, 3);
        testCase(flowGraph, 2, 3, 5);
        testCase(flowGraph, 12, 12, 24);
    }

}
