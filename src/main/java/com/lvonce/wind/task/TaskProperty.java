package com.lvonce.wind.task;

import java.util.BitSet;

public class TaskProperty {

    private static final int ASYNC_IDX = 0;
    private static final int BRANCH_IDX = 1;
    private static final int IDEMPOTENT_IDX = 2;
    private static final int LOCKABLE_IDX = 3;
    private static final int CANCELABLE_IDX = 4;

    private final BitSet properties = new BitSet();

    public void setAsync() {
        this.properties.set(ASYNC_IDX);
    }

    public void setBranch() {
        this.properties.set(BRANCH_IDX);
    }

    public void setIdempotent() {
        this.properties.set(IDEMPOTENT_IDX);
    }

    public void setLockable() {
        this.properties.set(LOCKABLE_IDX);
    }

    public void setCancelable() {
        this.properties.set(CANCELABLE_IDX);
    }

    boolean isAsync() {
        return this.properties.get(ASYNC_IDX);
    }

    boolean isBranch() {
        return this.properties.get(BRANCH_IDX);
    }

    boolean isIdempotent() {
        return this.properties.get(IDEMPOTENT_IDX);
    }

    boolean isLockable() {
        return this.properties.get(LOCKABLE_IDX);
    }

    boolean isCancelable() {
        return this.properties.get(CANCELABLE_IDX);
    }
}
