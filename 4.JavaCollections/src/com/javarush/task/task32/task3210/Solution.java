package com.javarush.task.task32.task3210;

import java.io.IOException;
import java.io.RandomAccessFile;

/* 
Используем RandomAccessFile
*/

public class Solution {
    public static void main(String... args) throws IOException {
        String fileName = args[0];
        long number = Long.parseLong(args[1]);
        String text = args[2];

        RandomAccessFile file = new RandomAccessFile(fileName, "rw");
        file.seek(number);

        byte[] buffer = new byte[text.length()];
        int count = file.read(buffer, 0, text.length());
        String s = new String(buffer);

        file.seek(file.length());
        if (text.equals(s))
            file.write("true".getBytes());
        else
            file.write("false".getBytes());

        file.close();
    }
}
