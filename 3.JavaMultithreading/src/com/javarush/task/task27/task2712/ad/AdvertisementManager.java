package com.javarush.task.task27.task2712.ad;

import com.javarush.task.task27.task2712.ConsoleHelper;
import com.javarush.task.task27.task2712.statistic.StatisticManager;
import com.javarush.task.task27.task2712.statistic.event.NoAvailableVideoEventDataRow;
import com.javarush.task.task27.task2712.statistic.event.VideoSelectedEventDataRow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvertisementManager {
    private final AdvertisementStorage storage = AdvertisementStorage.getInstance();
    private StatisticManager manager = StatisticManager.getInstance();

    private int timeSeconds;

    public AdvertisementManager(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void processVideos() {
        List<Advertisement> videos = storage.list();
        if (videos.isEmpty()) {
            throw new NoVideoAvailableException();
        }

        videos = setProfitableVideos();

        // Ведём статистику
        if (videos.isEmpty()) {
            manager.register(new NoAvailableVideoEventDataRow(timeSeconds));
        }

        long amount = 0;        // - общая сумма денег за рекламу (в копейках)
        int totalDuration = 0;  // - время на рекламу
        for (Advertisement adv : videos) {
            amount += adv.getAmountPerOneDisplaying();
            totalDuration += adv.getDuration();
        }
        manager.register(new VideoSelectedEventDataRow(videos, amount, totalDuration));

        for (Advertisement adv : videos) {
            ConsoleHelper.writeMessage(String.format("%s is displaying... %d, %d",
                    adv.getName(),
                    adv.getAmountPerOneDisplaying(),
                    (long) (((double) adv.getAmountPerOneDisplaying()) / adv.getDuration() * 1000)));
            adv.revalidate();
        }
    }

    private List<Advertisement> setProfitableVideos() {
        getSortListHits();
        List<List<Advertisement>> selectedVideoList = getSelectedVideoList(storage.list(), new ArrayList<>(), 0, timeSeconds);
        selectedVideoList = getMaxCashList(selectedVideoList);
        selectedVideoList = getMaxLongTimeVideoList(selectedVideoList);
        selectedVideoList = getMinSizeVideo(selectedVideoList);
        return showVideoList(selectedVideoList);
    }

    // Сортирует по продолжительности видео
    private void getSortListHits(){
        storage.list().sort(Comparator.comparingInt(Advertisement::getDuration));
    }

    // Список всех возможных вариантов cписков видео
    private List<List<Advertisement>> getSelectedVideoList(
            List<Advertisement> sortedList, List<Advertisement> list, int start, int endTime){
        List<List<Advertisement>> innerList = new ArrayList<>();
        List<List<Advertisement>> fakeList;
        for (int i = start; i < sortedList.size(); i++) {
            Advertisement advertisement = sortedList.get(i);
            if (endTime > 0
                    && advertisement.getDuration() <= endTime
                    && advertisement.getHits() > 0) {
                List<Advertisement> testList = new ArrayList<>(list);
                testList.add(advertisement);
                innerList.add(testList);
                if ((fakeList = getSelectedVideoList(sortedList, testList, i + 1,
                        endTime - advertisement.getDuration())) != null){
                    for (int j = 0; j < fakeList.size(); j++) {
                        innerList.add(fakeList.get(j));
                    }
                }
            }
        }
        return innerList;
    }

    private List<List<Advertisement>> getMaxCashList(List<List<Advertisement>> selectedVideoList) {
        long maxCash = 0;   // Максимальный кэш из всех списков
        for (List<Advertisement> list : selectedVideoList){
            long count = 0;
            for (int i = 0; i < list.size(); i++) {
                count += list.get(i).getAmountPerOneDisplaying();
            }
            if (count > maxCash) maxCash = count;
        }

        // Удаляем все списки, у которых кэш меньше максимального
        for (int i = 0; i < selectedVideoList.size(); i++) {
            long count = 0;
            for (int j = 0; j < selectedVideoList.get(i).size(); j++) {
                count += selectedVideoList.get(i).get(j).getAmountPerOneDisplaying();
            }
            if (count != maxCash){
                selectedVideoList.remove(i);
                i--;
            }
        }
        return selectedVideoList;
    }

    // Отбирает списки, у которых наибольшая продолжительность
    private List<List<Advertisement>> getMaxLongTimeVideoList(List<List<Advertisement>> selectedVideoList) {
        int maxLongTime = 0;
        for (List<Advertisement> list : selectedVideoList){
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                count += list.get(i).getDuration();
            }
            if (count > maxLongTime) maxLongTime = count;
        }

        for (int i = 0; i < selectedVideoList.size(); i++) {
            long count = 0;
            for (int j = 0; j < selectedVideoList.get(i).size(); j++) {
                count += selectedVideoList.get(i).get(j).getDuration();
            }
            if (count < maxLongTime){
                selectedVideoList.remove(i);
                i--;
            }
        }
        return selectedVideoList;
    }

    private List<List<Advertisement>> getMinSizeVideo(List<List<Advertisement>> selectedVideoList) {
        if (selectedVideoList.isEmpty()) {
            manager.register(new NoAvailableVideoEventDataRow(timeSeconds));
            throw new NoVideoAvailableException();
        }
        int minSizeVideo = selectedVideoList.get(0).size();
        for (int i = 0; i < selectedVideoList.size(); i++) {
            if (selectedVideoList.get(i).size() < minSizeVideo) minSizeVideo = selectedVideoList.get(i).size();
        }
        for (int i = 0; i < selectedVideoList.size(); i++) {
            if (selectedVideoList.get(i).size() != minSizeVideo){
                selectedVideoList.remove(i);
                i--;
            }
        }
        return selectedVideoList;
    }

    private List<Advertisement> showVideoList(List<List<Advertisement>> selectedVideoList) {
        List<Advertisement> list = selectedVideoList.get(selectedVideoList.size()-1);
        list.sort((o1, o2) -> {
            long one = o1.getAmountPerOneDisplaying();
            long two = o2.getAmountPerOneDisplaying();

            if (one == two) {
                one = o1.getDuration();
                two = o2.getDuration();
            }
            if (one == two) {
                one = o2.getHits();
                two = o1.getHits();
            }
            return -Long.compare(one, two);
        });
        return list;
    }
}
