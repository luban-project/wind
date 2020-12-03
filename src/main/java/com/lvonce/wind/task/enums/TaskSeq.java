package com.lvonce.wind.task.enums;

public enum TaskSeq {

    RUNNING(-1),
    END(-2),
    ABORT(-3),
    FAIL(-4);


    int value;

    TaskSeq(int val) {
        this.value = val;
    }

    public int getSeqValue() {
        return this.value;
    }

    @SuppressWarnings("unused")
    public static boolean isPass(int seq) {
        return seq >= 0;
    }

    public static boolean isEnd(int seq) {
        return END.value == seq;
    }

    public static boolean isAbort(int seq) {
        return ABORT.value == seq;
    }

    public static boolean isFail(int seq) {
        return FAIL.value == seq;
    }
}
