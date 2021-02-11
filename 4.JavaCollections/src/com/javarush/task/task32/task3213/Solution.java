package com.javarush.task.task32.task3213;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/* 
Шифр Цезаря
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        StringReader reader = new StringReader("Khoor#Dpljr#&C,₷B'3");
        System.out.println(decode(reader, -3));  //Hello Amigo #@)₴?$0
    }

    public static String decode(StringReader reader, int key) throws IOException {
        if (reader == null)
            return "";

        StringWriter writer = new StringWriter();
        char[] buffer = new char[1024];
        int count;
        while ((count = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, count);
        }
        String text = writer.toString();
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            char newChar = (char) (c + key);
            result.append(newChar);
        }
        return result.toString();
    }
}
