package com.javarush.task.task31.task3101;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) {
        File path = new File(args[0]);
        File res = new File(args[1]);
//        File path = new File("D:\\IdeaProjects\\dirForExample");
//        File res = new File("D:\\IdeaProjects\\res.txt");

        // Переименовываем файл
        File result = new File(res.getParent() + "/allFilesContent.txt");
        FileUtils.renameFile(res, result);

        // Получаем отсортированный список файлов в каталоге (@code path)
        List<File> files = getFiles(path);

        // Записываем в файл-результат содержимое файлов в отсортированном списке
        try (FileOutputStream writer = new FileOutputStream(result)){
            for (File file : files) {
                FileInputStream reader = new FileInputStream(file);
                while (reader.available() > 0) {
                    byte[] buffer = new byte[1000];
                    int count = reader.read(buffer);
                    writer.write(buffer, 0, count);
                    writer.flush();
                }
                writer.write((byte) '\n');
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<File> getFiles(File path) {
        List<File> files = new ArrayList<>();

        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(getFiles(file));
                continue;
            }
            if (file.length() <= 50) {
                files.add(file);
            }
        }
        files.sort(Comparator.comparing(File::getName));
        return files;
    }
}
