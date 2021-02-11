package com.javarush.task.task36.task3605;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

/* 
Использование TreeSet
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        TreeSet<Character> treeSet = new TreeSet<>();
        List<String> strings = Files.readAllLines(Paths.get(args[0]));
        for (String str : strings) {
            char[] chars = str.toLowerCase().toCharArray();
            for (char c : chars) {
                if (c >= 'a' && c <= 'z')
                    treeSet.add(c);
            }
        }
        for (int i = 0; i < 5; i++) {
            Character c = treeSet.pollFirst();
            if (c == null) break;
            System.out.print(c);
        }
    }
}
