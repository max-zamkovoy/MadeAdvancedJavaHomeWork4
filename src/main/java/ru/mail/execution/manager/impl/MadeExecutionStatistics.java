package ru.mail.execution.manager.impl;

import ru.mail.execution.RunnableDecorator;
import ru.mail.execution.manager.ExecutionStatistics;

import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class MadeExecutionStatistics implements ExecutionStatistics {

    private final List<RunnableDecorator> tasks;

    public MadeExecutionStatistics(List<RunnableDecorator> tasks) {
        this.tasks = Collections.unmodifiableList(tasks);
    }

    @Override
    public int getMinExecutionTimeInMs() {
        return tasks.stream()
                .filter(RunnableDecorator::isCompleted)
                .mapToInt(RunnableDecorator::getExecutionTimeInMs)
                .min()
                .orElse(0);
    }

    @Override
    public int getMaxExecutionTimeInMs() {
        return tasks.stream()
                .filter(RunnableDecorator::isCompleted)
                .mapToInt(RunnableDecorator::getExecutionTimeInMs)
                .max()
                .orElse(0);
    }

    @Override
    public int getAverageExecutionTimeInMs() {
        OptionalDouble avg = tasks.stream()
                .filter(RunnableDecorator::isCompleted)
                .mapToInt(RunnableDecorator::getExecutionTimeInMs)
                .average();
        return avg.isPresent() ? Double.valueOf(avg.getAsDouble()).intValue() : 0;
    }
}
