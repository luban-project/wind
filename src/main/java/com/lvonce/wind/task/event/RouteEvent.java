package com.lvonce.wind.task.event;

import lombok.Data;
import lombok.AllArgsConstructor;
import com.lvonce.wind.task.enums.TaskSeq;

@Data
@AllArgsConstructor
public class RouteEvent {
    final int currTaskSeq;
    final int nextTaskSeq;
    final boolean isAsync;
    final int retryCount;

    public static RouteEvent of(int currTaskSeq, int nextTaskSeq) {
        return of(currTaskSeq, nextTaskSeq, false);
    }

    public static RouteEvent of(int currTaskSeq, int nextTaskSeq, boolean isAsync) {
        return of(currTaskSeq, nextTaskSeq, isAsync, 0);
    }

    public static RouteEvent of(int currTaskSeq, int nextTaskSeq, boolean isAsync, int retryCount) {
        return new RouteEvent(
                currTaskSeq,
                nextTaskSeq,
                isAsync,
                retryCount
        );
    }
}
