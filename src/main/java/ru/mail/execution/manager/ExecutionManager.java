package ru.mail.execution.manager;

public interface ExecutionManager {

    Context execute(Runnable... tasks);
}
