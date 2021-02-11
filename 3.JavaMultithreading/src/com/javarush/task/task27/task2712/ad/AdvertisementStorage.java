package com.javarush.task.task27.task2712.ad;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementStorage {
    private static AdvertisementStorage storage = new AdvertisementStorage();

    private AdvertisementStorage() {
        Object someContent = new Object();
        videos.add(new Advertisement(someContent, "First video", 5000, 50, 3 * 60)); // 3 min
        videos.add(new Advertisement(someContent, "Secon video", 1000, 1, 15 * 60)); //15 min
        videos.add(new Advertisement(someContent, "Third video", 2000, 1, 10 * 60)); //10 min
    }

    public static AdvertisementStorage getInstance() {
        return storage;
    }

    private final List<Advertisement> videos = new ArrayList<Advertisement>();

    public List<Advertisement> list() {
        return videos;
    }

    public void add(Advertisement advertisement) {
        videos.add(advertisement);
    }
}
