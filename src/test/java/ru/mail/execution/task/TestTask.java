package ru.mail.execution.task;

import java.util.Random;

public class TestTask implements Runnable {

    public static final int MIN_TIME_TO_RUN_TASK = 100;
    public static final int MAX_TIME_TO_RUN_TASK = 500;

    @Override
    public void run() {
        try {
            Thread.sleep(randomTaskExecutionTime());
        } catch (InterruptedException ignored) {
        }
    }

    private int randomTaskExecutionTime() {
        return new Random().nextInt((MAX_TIME_TO_RUN_TASK - MIN_TIME_TO_RUN_TASK) + 1) + MIN_TIME_TO_RUN_TASK;
    }
}
