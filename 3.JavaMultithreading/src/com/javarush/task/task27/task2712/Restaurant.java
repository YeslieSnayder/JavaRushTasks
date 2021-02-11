package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Cook;
import com.javarush.task.task27.task2712.kitchen.Order;
import com.javarush.task.task27.task2712.kitchen.Waiter;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private final static int ORDER_CREATING_INTERVAL = 100;
    private final static LinkedBlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final static LinkedBlockingQueue<Order> doneOrderQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        List<Cook> cooks = new ArrayList<>();
        cooks.add(new Cook("P0vAp1337"));
        cooks.add(new Cook("Uo3aь"));

        StatisticManager manager = StatisticManager.getInstance();
        Waiter waiter = new Waiter();
        waiter.setQueue(doneOrderQueue);

        for (Cook cook : cooks) {
            cook.setQueue(orderQueue);
            cook.setDoneOrderQueue(doneOrderQueue);
        }

        List<Tablet> tablets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Tablet tablet = new Tablet(i);
            tablet.setQueue(orderQueue);
            tablets.add(tablet);
        }

        for (Cook cook : cooks) {
            Thread workCooks = new Thread(cook);
            workCooks.setDaemon(true);
            workCooks.start();
        }
        Thread waiterThread = new Thread(waiter);
        waiterThread.setDaemon(true);
        waiterThread.start();

        Thread thread = new Thread(new RandomOrderGeneratorTask(tablets, ORDER_CREATING_INTERVAL));
        thread.start();

        Thread.sleep(1000);
        thread.interrupt();

//        tablet.createOrder();
//        tablet.createOrder();
//        tablet.createOrder();
//        tablet.createOrder();

        DirectorTablet directorTablet = new DirectorTablet();

        directorTablet.printAdvertisementProfit();
        directorTablet.printCookWorkloading();
        directorTablet.printActiveVideoSet();
        directorTablet.printArchivedVideoSet();
    }
}
