package com.lvonce.wind.task.enums;

public enum FlowState {

    /**
     * any task be not-supported, the flow will be not-supported
     */
    NOT_SUPPORTED,

    /**
     * init state of a flow
     */
    EMPTY,

    /**
     * any task be pending, maybe success in the future
     */
    PENDING,

    /**
     * all the tasks in the path to an end be success
     */
    SUCCESS,

    /**
     * -------------- Simple Flow --------------------
     */
    FAIL, //any task be fail, only in simple flow/retry flow


    /**
     * -------------- Retry Flow ---------------------
     */
    ABORT, //any task reach the retry count limit, but don't know result


    CANCELLING;


    public static FlowState from(TaskState taskState) {
        switch (taskState) {
            case ABORT:
                return FlowState.ABORT;

            case SUCCESS:
            case LOCKED:
            case CONFIRMING:
            case PENDING:
                return FlowState.PENDING;

            case FAIL:
            case CANCELLING:
                return FlowState.CANCELLING;

            case NOT_SUPPORTED:
                return FlowState.NOT_SUPPORTED;
            default:
                return FlowState.EMPTY;
        }
    }

    public boolean isRunning() {
        return this.equals(PENDING) || this.equals(CANCELLING);
    }
}
