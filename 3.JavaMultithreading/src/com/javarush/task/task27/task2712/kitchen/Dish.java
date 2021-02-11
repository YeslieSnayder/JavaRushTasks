package com.javarush.task.task27.task2712.kitchen;

public enum Dish {
    Fish(25),
    Steak(30),
    Soup(15),
    Juice(5),
    Water(3);

    Dish(int duration) {
        this.duration = duration;
    }

    public static String allDishesToString() {
        StringBuilder sb = new StringBuilder();
        for (Dish dish : Dish.values()) {
            if (dish.ordinal() != 0) {
                sb.append(", ");
            }
            sb.append(dish);
        }
        return sb.toString();
    }

    public int getDuration() {
        return duration;
    }

    private int duration;
}
