package ru.mail.execution;

import org.junit.Before;
import org.junit.Test;
import ru.mail.execution.manager.Context;
import ru.mail.execution.manager.impl.MadeExecutionManager;
import ru.mail.execution.task.TaskFactory;

import static org.junit.Assert.assertTrue;
import static ru.mail.execution.task.TestTask.MAX_TIME_TO_RUN_TASK;

public class ExecutionStatisticsTest {

    private MadeExecutionManager executionManager;
    private TaskFactory taskFactory;

    @Before
    public void setUp() {
        executionManager = new MadeExecutionManager();
        taskFactory = new TaskFactory();
    }

    @Test
    public void getMinExecutionTimeInMs() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = executionManager.execute(tasks);
        waiteFinish();
        long result = context.getStatistics().getMinExecutionTimeInMs();
        assertTrue(result > 0);
    }

    @Test
    public void getMaxExecutionTimeInMs() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = executionManager.execute(tasks);
        waiteFinish();
        int result = context.getStatistics().getMaxExecutionTimeInMs();
        assertTrue(result > 0);
    }

    @Test
    public void getAverageExecutionTimeInMs() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = executionManager.execute(tasks);
        waiteFinish();
        int average = context.getStatistics().getAverageExecutionTimeInMs();
        int min = context.getStatistics().getMinExecutionTimeInMs();
        int max = context.getStatistics().getMaxExecutionTimeInMs();
        assertTrue(average > 0);
        assertTrue(average > min);
        assertTrue(average < max);
    }

    private void waiteFinish() {
        try {
            Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
