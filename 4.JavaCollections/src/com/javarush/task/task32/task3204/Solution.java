package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* 
Генератор паролей
*/
public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() {
        char[] symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        List<Character> password = new ArrayList<>();

        char lower = (char) ('a' + (int) (Math.random() * ('z' - 'a')));
        char upper = (char) ('A' + (int) (Math.random() * ('Z' - 'A')));
        char number = symbols[(int) (Math.random() * 10)];

        password.add(lower);
        password.add(upper);
        password.add(number);

        for (int i = 3; i < 8; i++)
            password.add(symbols[(int) (Math.random() * symbols.length)]);

        Collections.shuffle(password);
        for (int i = 0; i < password.size(); i++) {
            result.write(password.get(i));
        }
        return result;
    }
}