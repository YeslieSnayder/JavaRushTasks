package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
*/
public class Solution {

    private static Map<ZipEntry, byte[]> mapFromZip = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path fileToZip = Paths.get(args[0]);
        String nameZipFile = args[1];

        fillMap(nameZipFile);
        fromMapToFile(nameZipFile, fileToZip);
    }

    private static void fillMap(String nameZipFile) throws IOException {
        try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(nameZipFile))) {
            ZipEntry entry;
            while ((entry = inputStream.getNextEntry()) != null) {
                mapFromZip.put(entry, getBiteArrayFromInputStream(inputStream));
            }
        }
    }

    private static byte[] getBiteArrayFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;

        while ((count = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, count);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private static void fromMapToFile(String nameZipFile, Path fileToZip) throws IOException {
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(nameZipFile))) {
            Path newPath = Paths.get("new", fileToZip.getFileName().toString());
            ZipEntry newEntry = new ZipEntry(newPath.toString());
            outputStream.putNextEntry(new ZipEntry(newPath.toString()));
            Files.copy(fileToZip, outputStream);
//            try (InputStream inputStream = new FileInputStream(fileToZip.toFile())){
//                outputStream.write(getBiteArrayFromInputStream(inputStream));
//            }
//            outputStream.closeEntry();

            boolean isExist = false;
            for (Map.Entry<ZipEntry, byte[]> entry : mapFromZip.entrySet()) {
                ZipEntry key = entry.getKey();
                byte[] value = entry.getValue();

                if (key.getName().equals(newPath.toString())) {
                    isExist = true;
                } else {
                    outputStream.putNextEntry(key);
                    outputStream.write(value);
                    outputStream.flush();
                    outputStream.closeEntry();
                }
            }
            if (isExist) {
                outputStream.putNextEntry(newEntry);
                Files.copy(fileToZip, outputStream);
            }
        }
    }
}
