package ru.mail.execution.task;

public class FailedTask implements Runnable {

    @Override
    public void run() {
        throw new RuntimeException("Exception");
    }
}
