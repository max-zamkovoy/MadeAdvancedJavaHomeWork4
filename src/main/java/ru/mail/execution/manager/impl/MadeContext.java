package ru.mail.execution.manager.impl;

import ru.mail.execution.RunnableDecorator;
import ru.mail.execution.manager.Context;
import ru.mail.execution.manager.ExecutionStatistics;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class MadeContext implements Context {

    private final List<RunnableDecorator> tasks;
    private final AtomicInteger interruptedCount = new AtomicInteger();

    public MadeContext(List<RunnableDecorator> tasks) {
        this.tasks = Collections.unmodifiableList(tasks);
    }

    @Override
    public int getCompletedTaskCount() {
        int completedTasksCount = tasks.stream()
                .map(RunnableDecorator::getResult)
                .filter(CompletableFuture::isDone)
                .map(e -> 1)
                .reduce(0, Integer::sum);
        return completedTasksCount - getInterruptedTaskCount() - getFailedTaskCount();
    }

    @Override
    public int getFailedTaskCount() {
        int completedExceptionallyCount = tasks.stream()
                .map(RunnableDecorator::getResult)
                .filter(CompletableFuture::isCompletedExceptionally)
                .map(e -> 1)
                .reduce(0, Integer::sum);
        return completedExceptionallyCount - getInterruptedTaskCount();
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedCount.get();
    }

    @Override
    public void interrupt() {
        tasks.stream()
                .filter(RunnableDecorator::isNew)
                .map(RunnableDecorator::getResult)
                .forEach(result -> {
                    result.cancel(false);
                    interruptedCount.getAndIncrement();
                });
    }

    @Override
    public boolean isFinished() {
        return tasks.size() == getInterruptedTaskCount() + getCompletedTaskCount();
    }

    @Override
    public void onFinish(Runnable callback) {
        CompletableFuture.allOf(tasks.stream()
                .map(RunnableDecorator::getResult).toArray(CompletableFuture[]::new))
                .whenCompleteAsync((res, throwable) -> callback.run());
    }

    @Override
    public ExecutionStatistics getStatistics() {
        return new MadeExecutionStatistics(tasks);
    }

    @Override
    public void awaitTermination() {
        tasks.stream()
                .map(RunnableDecorator::getResult)
                .forEach(CompletableFuture::join);
    }
}
