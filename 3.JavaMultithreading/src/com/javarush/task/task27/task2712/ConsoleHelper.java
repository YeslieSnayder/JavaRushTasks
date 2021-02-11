package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.kitchen.Dish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() throws IOException {
        return reader.readLine();
    }

    public static List<Dish> getAllDishesForOrder() throws IOException {
        List<Dish> dishes = new ArrayList<>();

        writeMessage(Dish.allDishesToString());
        writeMessage("Введите название блюда");

        while (true) {
            String dish = readString();
            if (dish.equals("exit")) break;

            boolean found = false;
            for (Dish d : Dish.values()) {
                if (dish.equals(d.name())) {
                    dishes.add(d);
                    found = true;
                    break;
                }
            }
            if (!found) {
                writeMessage("Такого блюда нет");
            }
        }

        return dishes;
    }
}
