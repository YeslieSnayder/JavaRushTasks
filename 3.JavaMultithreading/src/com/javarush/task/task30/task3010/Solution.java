package com.javarush.task.task30.task3010;

/* 
Минимальное допустимое основание системы счисления
*/

public class Solution {
    public static void main(String[] args) {
        try {
            String str = args[0];
            char[] chars = str.toLowerCase().toCharArray();
            char maxLetter = 0;
            for (char c : chars) {
                if (!((c > 96) && (c < 123)
                        || (c > 47) && (c < 58))) {
                    System.out.println("incorrect");
                    return;
                }
                if (c > maxLetter)
                    maxLetter = c;
            }

            if (Character.isDigit(maxLetter))
                System.out.println(maxLetter > 49 ? String.valueOf(maxLetter - 47) : "2");
            else
                System.out.println(maxLetter - 86);
        } catch (Exception e) {}
    }
}