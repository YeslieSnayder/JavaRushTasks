package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;

import java.util.concurrent.LinkedBlockingQueue;

public class Waiter implements Runnable {
    private LinkedBlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        try {
            Order order = queue.take();
            ConsoleHelper.writeMessage(order + " was cooked for " + order.getTablet().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }
}
