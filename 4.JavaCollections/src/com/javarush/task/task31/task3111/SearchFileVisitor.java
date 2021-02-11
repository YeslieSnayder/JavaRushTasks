package com.javarush.task.task31.task3111;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {
    private int minSize;
    private int maxSize;
    private String partOfName;
    private String partOfContent;

    private boolean isAddMinSize = false;
    private boolean isAddMaxSize = false;
    private boolean isAddPartOfName = false;
    private boolean isAddPartOfContent = false;

    private List<Path> foundFiles = new ArrayList<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        byte[] content = Files.readAllBytes(file); // размер файла: content.length

        if ((!isAddMinSize || content.length > minSize) &&
                (!isAddMaxSize || content.length < maxSize) &&
                isPartOfName(file) &&
                isPartOfContent(file))
            foundFiles.add(file);

        return super.visitFile(file, attrs);
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        isAddMinSize = true;
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        isAddMaxSize = true;
        this.maxSize = maxSize;
    }

    public String getPartOfName() {
        return partOfName;
    }

    public void setPartOfName(String partOfName) {
        isAddPartOfName = true;
        this.partOfName = partOfName;
    }

    public String getPartOfContent() {
        return partOfContent;
    }

    public void setPartOfContent(String partOfContent) {
        isAddPartOfContent = true;
        this.partOfContent = partOfContent;
    }

    public List<Path> getFoundFiles() {
        return foundFiles;
    }

    private boolean isPartOfName(Path file) {
        return !isAddPartOfName || file.getFileName().toString().contains(partOfName);
    }

    private boolean isPartOfContent(Path file) throws IOException {
        if (!isAddPartOfContent) return true;

        for (String str : Files.readAllLines(file)) {
            if (str.contains(partOfContent)) {
                return true;
            }
        }
        return false;
    }
}
