package com.javarush.task.task31.task3113;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileDirectoryVisitor extends SimpleFileVisitor<Path> {
    private int folders = 0;
    private int files = 0;
    private long size = 0;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        files++;
        size += Files.size(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        folders++;
        return FileVisitResult.CONTINUE;
    }

    public int getFolders() {
        return folders;
    }

    public int getFiles() {
        return files;
    }

    public long getSize() {
        return size;
    }
}
