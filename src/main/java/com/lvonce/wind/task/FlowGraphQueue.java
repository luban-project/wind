//package com.lvonce.wind.task;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//import java.util.List;
//import java.util.concurrent.ConcurrentLinkedDeque;
//
//public class FlowGraphQueue {
//
//    @Data
//    @AllArgsConstructor
//    public static class Element {
//        private SimpleFlowGraph flow;
//        private List<DataEvent> events;
//    }
//
//    private ConcurrentLinkedDeque<Element> asyncQueue = new ConcurrentLinkedDeque<>();
//    private ConcurrentLinkedDeque<Element> retryQueue = new ConcurrentLinkedDeque<>();
//    private ConcurrentLinkedDeque<Element> monitorQueue = new ConcurrentLinkedDeque<>();
//
//    public void submitAsyncFlowGraph(SimpleFlowGraph flow, List<DataEvent> events) {
//        Element element = new Element(flow, events);
//        asyncQueue.addLast(element);
//    }
//
//    public void submitRetryFlowGraph(SimpleFlowGraph flow, List<DataEvent> events) {
//        Element element = new Element(flow, events);
//        retryQueue.addLast(element);
//    }
//
//
//    public Element pull() {
//        Element element = asyncQueue.pollFirst();
//        if (element != null) {
//            return element;
//        }
//
//        element = retryQueue.pollFirst();
//        if (element != null) {
//            return element;
//        }
//
//        element = monitorQueue.pollFirst();
//        return element;
//    }
//
//    public void registerMonitor(Monitor monitor) {
//        Thread thread = new Thread(()->{
//            while (true) {
//                List<Element> elements = monitor.pull();
//                for (Element element : elements) {
//                    monitorQueue.addLast(element);
//                }
//                Long sleepTimeMills = monitor.sleepTimeMills();
//                try {
//                    if (sleepTimeMills != null) {
//                        Thread.sleep(sleepTimeMills);
//                    }
//                } catch (Exception ex) {
//                }
//            }
//        });
//        thread.start();
//    }
//
//}
