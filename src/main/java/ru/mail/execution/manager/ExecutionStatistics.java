package ru.mail.execution.manager;

public interface ExecutionStatistics {

    int getMinExecutionTimeInMs();

    int getMaxExecutionTimeInMs();

    int getAverageExecutionTimeInMs();
}