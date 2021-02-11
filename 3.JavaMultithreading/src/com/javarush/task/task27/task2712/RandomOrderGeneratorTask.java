package com.javarush.task.task27.task2712;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomOrderGeneratorTask implements Runnable {
    private int interval;
    private List<Tablet> tablets;

    public RandomOrderGeneratorTask(List<Tablet> tablets, int interval) {
        this.tablets = tablets;
        this.interval = interval;
    }

    @Override
    public void run() {
        if (tablets.size() == 0) return;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int randNum = ThreadLocalRandom.current().nextInt(tablets.size());
//                int randNum = (int) (Math.random() * tablets.size());
                Tablet tablet = tablets.get(randNum);
                tablet.createTestOrder();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
