package com.javarush.task.task28.task2805;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {
    private final int Max_Priority = (this.getThreadGroup() != null) ? getThreadGroup().getMaxPriority() : MAX_PRIORITY;
    private static AtomicInteger counterPriority = new AtomicInteger(MIN_PRIORITY);

    public MyThread() {
        super();
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(Runnable target) {
        super(target);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(ThreadGroup group, Runnable target) {
        super(group, target);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(String name) {
        super(name);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(ThreadGroup group, String name) {
        super(group, name);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(Runnable target, String name) {
        super(target, name);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }

    public MyThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
        int priority = counterPriority.getAndIncrement();
        if (priority > MAX_PRIORITY) priority %= MAX_PRIORITY;
        if (priority > Max_Priority) priority = Max_Priority;
        if (priority == 0) priority = Max_Priority;
        setPriority(priority);
    }
}
