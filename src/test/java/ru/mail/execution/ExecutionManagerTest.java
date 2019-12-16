package ru.mail.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.mail.execution.manager.Context;
import ru.mail.execution.manager.impl.MadeExecutionManager;
import ru.mail.execution.task.TaskFactory;

import static org.junit.Assert.*;
import static ru.mail.execution.task.TaskFactory.TASKS_COUNT;
import static ru.mail.execution.task.TestTask.MAX_TIME_TO_RUN_TASK;

public class ExecutionManagerTest {

    private TaskFactory taskFactory;
    private MadeExecutionManager target;

    @Before
    public void setUp() {
        taskFactory = new TaskFactory();
        target = new MadeExecutionManager();
    }

    @Test
    public void interruptTasks() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = target.execute(tasks);
        assertEquals(0, context.getInterruptedTaskCount());
        context.interrupt();
        assertTrue(context.getInterruptedTaskCount() > 0);
    }

    @Test
    public void failTasks() {
        Runnable[] tasks = taskFactory.createFailedTasks();
        Context context = target.execute(tasks);
        waitFinish();
        assertEquals(2, context.getFailedTaskCount());
    }

    @Test
    public void getCompletedTasks() {
        Runnable[] tasks = taskFactory.createFailedTasks();
        Context context = target.execute(tasks);
        context.interrupt();
        waitFinish();
        assertEquals(TASKS_COUNT - context.getFailedTaskCount() - context.getInterruptedTaskCount(),
                context.getCompletedTaskCount());
    }

    @Test
    public void finishedTasks() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = target.execute(tasks);
        waitFinish();
        assertTrue(context.isFinished());
    }

    @Test
    public void notFinished() {
        Runnable[] tasks = taskFactory.createFailedTasks();
        Context context = target.execute(tasks);
        waitFinish();
        assertFalse(context.isFinished());
    }

    @Test
    public void onFinish() {
        Runnable callBack = Mockito.mock(Runnable.class);
        Runnable[] tasks = taskFactory.createTasks();
        Context context = target.execute(tasks);
        context.onFinish(callBack);
        waitFinish();
        Mockito.verify(callBack).run();
    }

    @Test
    public void awaitTermination() {
        Runnable[] tasks = taskFactory.createTasks();
        Context context = target.execute(tasks);
        assertFalse(context.isFinished());
        context.awaitTermination();
        assertTrue(context.isFinished());
    }

    private void waitFinish() {
        try {
            Thread.sleep(MAX_TIME_TO_RUN_TASK * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
