package com.javarush.task.task27.task2712.kitchen;

import com.javarush.task.task27.task2712.Tablet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TestOrder extends Order {
    public TestOrder(Tablet tablet) throws IOException {
        super(tablet);
    }

    @Override
    protected void initDishes() throws IOException {
        dishes = new ArrayList<>();
        Random random = new Random();
        int countOfDishes = random.nextInt(5) + 1;

        for (int i = 0; i < countOfDishes; i++) {
            int dishValue = random.nextInt(Dish.values().length);
            Dish dish = Dish.values()[dishValue];
            dishes.add(dish);
        }
    }
}
