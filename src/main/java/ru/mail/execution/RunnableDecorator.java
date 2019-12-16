package ru.mail.execution;

import java.util.concurrent.CompletableFuture;

public class RunnableDecorator implements Runnable {

    private final Runnable wrapped;
    private boolean isStarted = false;
    private int executionTimeInMs;
    private CompletableFuture<Void> result;

    public RunnableDecorator(Runnable wrapped) {
        this.wrapped = wrapped;
    }

    public void run() {
        isStarted = true;
        long startTime = System.currentTimeMillis();
        wrapped.run();
        long stopTime = System.currentTimeMillis();
        executionTimeInMs = (int)((stopTime - startTime) % Integer.MAX_VALUE);
    }

    public int getExecutionTimeInMs() {
        return executionTimeInMs;
    }

    public CompletableFuture<Void> getResult() {
        return result;
    }

    public void setResult(CompletableFuture<Void> result) {
        this.result = result;
    }

    public boolean isNew() {
        return !isStarted;
    }

    public boolean isCompleted() {
        return result.isDone() && !result.isCompletedExceptionally();
    }
}
