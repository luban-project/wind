package com.lvonce.wind.task.enums;

public enum TaskState {
    NOT_SUPPORTED,

//    /** -------------- Saga Task -----------------------
//     * EMPTY ---> PENDING ---> SUCCESS ---> CANCELLING
//     *              |                           |
//     *              |                           |
//     *              | ------>   FAIL      <------
//     * -------------------------------------------------*/
//
//
//    /** -------------- Tcc Task ------------------------------
//     *                            ---> CONFIRMING ---> SUCCESS
//     *                           |
//     *                           |
//     * EMPTY ---> PENDING ---> LOCKED ---> CANCELLING
//     *              |                           |
//     *              |                           |
//     *              | ------>   FAIL      <------
//     * ------------------------------------------------------*/

    EMPTY,
    LOCKED,
    PENDING,
    SUCCESS,
    FAIL,
    CONFIRMING,
    CANCELLING,
    ABORT;

    public boolean isRunning() {
        return this.equals(PENDING) || this.equals(CONFIRMING) || this.equals(CANCELLING);
    }

    public boolean isFinish() {
        return this.equals(SUCCESS) || this.equals(FAIL) || this.equals(LOCKED);
    }

    public boolean isAbort() {
        return this.equals(ABORT);
    }
}
