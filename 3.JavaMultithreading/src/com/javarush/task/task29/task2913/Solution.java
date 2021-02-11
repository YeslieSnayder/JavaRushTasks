package com.javarush.task.task29.task2913;

import java.util.Random;

/* 
Замена рекурсии
*/

public class Solution {
    private static int numberA;
    private static int numberB;

    public static String getAllNumbersBetween(int a, int b) {
        StringBuilder sb = new StringBuilder();

        if (a < b) {
            int min = a;
            for (; min < b; min++) {
                sb.append(min);
                sb.append(" ");
            }
        } else if (a > b) {
            int max = a;
            for (; max > b; max--) {
                sb.append(max);
                sb.append(" ");
            }
        } else
            sb.append(a);

        sb.append(b);
        return sb.toString();
    }

    public static void main(String[] args) {
        Random random = new Random();
        numberA = random.nextInt(100);
        numberB = random.nextInt(1000);
        System.out.println(getAllNumbersBetween(numberA, numberB));
        System.out.println(getAllNumbersBetween(numberB, numberA));
    }
}