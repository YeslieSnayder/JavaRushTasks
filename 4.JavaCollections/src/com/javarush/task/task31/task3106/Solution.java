package com.javarush.task.task31.task3106;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipInputStream;

/*
Разархивируем файл
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        String nameResultFile = args[0];

        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            fileNames.add(args[i]);
        }
        Collections.sort(fileNames);

        Vector<FileInputStream> files = new Vector<>();
        for (String s : fileNames) {
            files.addElement(new FileInputStream(s));
        }

        try (ZipInputStream inputStream = new ZipInputStream(new SequenceInputStream(files.elements()));
             FileOutputStream outputStream = new FileOutputStream(nameResultFile)) {
            while (inputStream.getNextEntry() != null) {
                byte[] buffer = new byte[1024];
                int count;

                while ((count = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, count);
                }
            }
        }
    }
}
