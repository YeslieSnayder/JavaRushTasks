package com.javarush.task.task37.task3703;

import java.util.concurrent.ConcurrentSkipListMap;

/*
Найти класс по описанию
*/

//1. Реализует интерфейс Map.
//2. Используется при работе с трэдами.
//3. Является неблокирующей версией списка с пропусками, который адаптирован для хеш-таблицы.

//1. Класс возвращенный методом getExpectedClass должен быть потомком класса AbstractMap.
//2. Класс возвращенный методом getExpectedClass должен поддерживать интерфейс ConcurrentNavigableMap.
//3. Класс возвращенный методом getExpectedClass должен поддерживать интерфейс Serializable.
//4. Класс возвращенный методом getExpectedClass должен поддерживать интерфейс Cloneable.

public class Searcher {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        return ConcurrentSkipListMap.class;
    }
}
