package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.StatisticAdvertisementManager;
import com.javarush.task.task27.task2712.statistic.StatisticManager;

import java.text.SimpleDateFormat;
import java.util.*;

public class DirectorTablet {
    private StatisticManager manager = StatisticManager.getInstance();
    private StatisticAdvertisementManager advManager = StatisticAdvertisementManager.getInstance();

    void printAdvertisementProfit() {
        TreeMap<Date, Double> map = new TreeMap<>(manager.getAdvertisementProfit());
        if (map.isEmpty()) return;
        Double total = 0.0;

        ArrayList<Date> keys = new ArrayList<>(map.keySet());
        for (int i = keys.size()-1; i >= 0; i--) {
            Date date = keys.get(i);
            Double amount = map.get(date);

            SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
            ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "%s - %.2f", format.format(date), amount));

            total += amount;
        }
        ConsoleHelper.writeMessage(String.format(Locale.ENGLISH, "Total - %.2f", total));
    }

    void printCookWorkloading() {
        TreeMap<Date, Map<String, Integer>> map = new TreeMap<>(manager.getCookWorkloading());
        if (map.isEmpty()) return;

        ArrayList<Date> keys = new ArrayList<>(map.keySet());
        for (int i = keys.size() - 1; i >= 0; i--) {
            Date date = keys.get(i);
            TreeMap<String, Integer> cookMap = new TreeMap<>(map.get(date));

            SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
            ConsoleHelper.writeMessage(format.format(date));

            for (Map.Entry<String, Integer> entry : cookMap.entrySet()) {
                String name = entry.getKey();
                int workloading = entry.getValue();
                if (workloading == 0) continue;

                ConsoleHelper.writeMessage(String.format("%s - %d min", name, workloading));
            }
            ConsoleHelper.writeMessage("");
        }
    }

    void printActiveVideoSet() {
        Map<String, Integer> map = advManager.getVideoSet();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String name = entry.getKey();
            int hits = entry.getValue();

            if (hits > 0)
                ConsoleHelper.writeMessage(String.format("%s - %d", name, hits));
        }
    }

    void printArchivedVideoSet() {
        Map<String, Integer> map = advManager.getVideoSet();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String name = entry.getKey();
            int hits = entry.getValue();

            if (hits == 0)
                ConsoleHelper.writeMessage(name);
        }
    }
}
