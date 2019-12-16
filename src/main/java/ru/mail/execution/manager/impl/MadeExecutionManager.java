package ru.mail.execution.manager.impl;

import ru.mail.execution.RunnableDecorator;
import ru.mail.execution.manager.Context;
import ru.mail.execution.manager.ExecutionManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MadeExecutionManager implements ExecutionManager {

    private static final int NUMBER_OF_THREADS_IN_POOL = 15;

    @Override
    public Context execute(Runnable... tasks) {
        List<RunnableDecorator> taskDecorators = Arrays.stream(tasks)
                .map(RunnableDecorator::new)
                .collect(Collectors.toList());
        runTasks(taskDecorators);
        return new MadeContext(taskDecorators);
    }

    private void runTasks(List<RunnableDecorator> taskList) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS_IN_POOL);
        taskList.forEach(task -> task.setResult(CompletableFuture.runAsync(task, executor)));
    }
}
