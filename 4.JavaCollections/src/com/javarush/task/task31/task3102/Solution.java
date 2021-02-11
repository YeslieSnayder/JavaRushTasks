package com.javarush.task.task31.task3102;

import java.io.File;
import java.io.IOException;
import java.util.*;

/* 
Находим все файлы
*/
public class Solution {
    public static List<String> getFileTree(String root) throws IOException {
        List<String> list = new ArrayList<>();
        Queue<File> queue = new PriorityQueue<>();
        File rootFile = new File(root);

        Collections.addAll(queue, rootFile.listFiles());

        while (!queue.isEmpty()) {
            File file = queue.remove();

            if (file.isDirectory())
                Collections.addAll(queue, file.listFiles());
            else
                list.add(file.getAbsolutePath());
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        List<String> list = getFileTree("D:\\IdeaProjects\\dirForExample");

        list.forEach(System.out::println);
    }
}
