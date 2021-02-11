package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/* 
Что внутри папки?
*/
public class Solution {

    public static void main(String[] args) throws IOException {
        Path directory;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            String nameDirectory = reader.readLine();
            directory = Paths.get(nameDirectory);
            if (!Files.isDirectory(directory)) {
                System.out.println(directory.toString() + " - не папка");
                return;
            }
        }

        FileDirectoryVisitor visitor = new FileDirectoryVisitor();
        Files.walkFileTree(directory, visitor);

        System.out.println("Всего папок - " + (visitor.getFolders() - 1));
        System.out.println("Всего файлов - " + visitor.getFiles());
        System.out.println("Общий размер - " + visitor.getSize());
    }
}
