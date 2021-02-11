package com.javarush.task.task27.task2712.ad;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatisticAdvertisementManager {
    private static StatisticAdvertisementManager manager = new StatisticAdvertisementManager();

    private StatisticAdvertisementManager() {}

    public static StatisticAdvertisementManager getInstance() {
        return manager;
    }

    private AdvertisementStorage storage = AdvertisementStorage.getInstance();

    public Map<String, Integer> getVideoSet() {
        Map<String, Integer> map = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
        List<Advertisement> list = storage.list();
        if (list.isEmpty()) return map;

        for (Advertisement adv : list)
            map.put(adv.getName(), adv.getHits());

        return map;
    }
}
