package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;

public class Cook extends Observable implements Runnable {
    private String name;
    private boolean busy;

    private LinkedBlockingQueue<Order> queue;
    private LinkedBlockingQueue<Order> doneOrderQueue;

    public Cook(String name) {
        this.name = name;
    }

    public void startCookingOrder(Order order) {
        busy = true;
        try {
            int timeCooking = order.getTotalCookingTime();
            ConsoleHelper.writeMessage(order + ", cooking time " + timeCooking + "min");

            // Ведём статистику
            StatisticManager.getInstance().register(new CookedOrderEventDataRow(
                    order.getTablet().toString(),
                    name,
                    timeCooking * 60,
                    order.getDishes()
            ));

            Thread.sleep(timeCooking * 10);

            doneOrderQueue.add(order);
        } catch (InterruptedException e) { }
        busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setQueue(LinkedBlockingQueue<Order> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                while (queue.isEmpty())
                    Thread.sleep(10);
                if (!isBusy())
                    startCookingOrder(queue.take());
            }
        } catch (InterruptedException e) {}
    }

    public void setDoneOrderQueue(LinkedBlockingQueue<Order> doneOrderQueue) {
        this.doneOrderQueue = doneOrderQueue;
    }
}
