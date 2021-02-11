package com.javarush.task.task27.task2712.statistic;

import com.javarush.task.task27.task2712.statistic.event.CookedOrderEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventDataRow;
import com.javarush.task.task27.task2712.statistic.event.EventType;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticManager {
    private static StatisticManager manager = new StatisticManager();
    private StatisticStorage statisticStorage = new StatisticStorage();

    private StatisticManager() {}

    public static StatisticManager getInstance() {
        return manager;
    }

    public void register(EventDataRow event) {
        statisticStorage.put(event);
    }

    public StatisticStorage getStatisticStorage() {
        return statisticStorage;
    }

    // Возвращает отсортированную карту
    // формата ('День, когда была реклама', 'Сумма, полученная за рекламу в этот день')
    public Map<Date, Double> getAdvertisementProfit() {
        Map<Date, Double> map = new TreeMap<>();

        // Заполняем список данными, относящимся к отображению рекламы
        List<VideoSelectedEventDataRow> advList = new ArrayList<>();
        for (EventDataRow data : statisticStorage.getStorage().get(EventType.SELECTED_VIDEOS)) {
            advList.add((VideoSelectedEventDataRow) data);
        }
        if (advList.isEmpty()) return map;

        for (VideoSelectedEventDataRow data : advList) {
            DateFormat format = new SimpleDateFormat("dd/MMMMM/yyyy");
            Date date = null;
            try {
                date = format.parse(format.format(data.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Double amount = ((Long)data.getAmount()).doubleValue() / 100;
            if (map.containsKey(date))
                map.put(date, map.get(date) + amount);
            else
                map.put(date, amount);
        }
        return map;
    }

    // Возвращает отсортированную карту
    // формата ('День, когда работал повар', 'Карта с поварами, работающими в этот день')
    public Map<Date, Map<String, Integer>> getCookWorkloading() {
        Map<Date, Map<String, Integer>> map = new TreeMap<>();
        Map<String, Integer> cookMap;

        // Заполняем список данными, относящимся к работе повара
        List<CookedOrderEventDataRow> cookList = new ArrayList<>();
        for (EventDataRow data : statisticStorage.getStorage().get(EventType.COOKED_ORDER)) {
            cookList.add((CookedOrderEventDataRow) data);
        }
        if (cookList.isEmpty()) return map;

        // Заполняем карту данными из списка
        for (CookedOrderEventDataRow data : cookList) {
            DateFormat format = new SimpleDateFormat("dd/MMMMM/yyyy");
            Date date = null;
            try {
                date = format.parse(format.format(data.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String cookName = data.getCookName();
            int workingTime = data.getTime();

            if (!map.containsKey(date)) {
                cookMap = new HashMap<>();
                cookMap.put(cookName, workingTime);
                map.put(date, cookMap);
            } else {
                cookMap = map.get(date);
                if (!cookMap.containsKey(cookName)) {
                    cookMap.put(cookName, workingTime);
                    map.put(date, cookMap);
                } else {
                    cookMap.put(cookName, cookMap.get(cookName) + workingTime);
                    map.put(date, cookMap);
                }
            }
        }

        // Меняем время работы повара (секунды -> минуты)
        // демаем это после заполнения списка, чтобы избежать проблем с округлением
        for (Map.Entry<Date, Map<String, Integer>> entry : map.entrySet()) {
            cookMap = map.get(entry.getKey());
            for (Map.Entry<String, Integer> e : cookMap.entrySet()) {
                int workingTime = (int) Math.ceil(e.getValue() / 60.0);
                cookMap.put(e.getKey(), workingTime);
            }
            map.put(entry.getKey(), cookMap);
        }

        return map;
    }

    private class StatisticStorage {
        private Map<EventType, List<EventDataRow>> storage;

        StatisticStorage() {
            storage = new HashMap<>();
            for (EventType event : EventType.values()) {
                storage.put(event, new ArrayList<EventDataRow>());
            }
        }

        private void put(EventDataRow data) {
            storage.get(data.getType()).add(data);
        }

        public Map<EventType, List<EventDataRow>> getStorage() {
            return storage;
        }
    }
}
