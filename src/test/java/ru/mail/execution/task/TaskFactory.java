package ru.mail.execution.task;

import java.util.stream.IntStream;

public class TaskFactory {

    public static final int TASKS_COUNT = 20;


    public Runnable[] createTasks() {
        return IntStream.rangeClosed(1, TASKS_COUNT)
                .mapToObj(i -> new TestTask())
                .toArray(Runnable[]::new);
    }

    public Runnable[] createFailedTasks() {
        Runnable[] tasks = createTasks();
        tasks[0] = new FailedTask();
        tasks[1] = new FailedTask();
        return tasks;
    }
}
