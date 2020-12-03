package com.lvonce.wind.task.enums;

public enum BranchNode {
    DEFAULT(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),


    END(13),
    FAIL(14),
    ABORT(15),
    MAX(16);

    public final int val;

    BranchNode(int val) {
        this.val = val;
    }
}
