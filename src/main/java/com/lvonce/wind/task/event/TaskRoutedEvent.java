package com.lvonce.wind.task.event;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.time.Instant;
import java.sql.Timestamp;

/**
 * produced by task,
 * a task always receive a TaskEvent, and send a TaskEvent
 *
 * @param <T> the data payload type
 */
@Data
@AllArgsConstructor
public class TaskRoutedEvent<T> {

    final String id;
    final Timestamp time;
    final String source;
    final String subject;
    final String specversion;
    final String type;
    final String datacontenttype;
    final TaskEvent<T> taskevent;
    final RouteEvent routeevent;

    public static <T> TaskRoutedEvent<T> of(TaskEvent<T> taskEvent, RouteEvent routeEvent) {
        return new TaskRoutedEvent<>(
                UUID.randomUUID().toString(),
                Timestamp.from(Instant.now()),
                "system",
                "system",
                "1.0",
                "system",
                "json",
                taskEvent, routeEvent
        );
    }
}
