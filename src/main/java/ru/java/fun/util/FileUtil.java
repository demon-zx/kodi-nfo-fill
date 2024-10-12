package ru.java.fun.util;

import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.Optional;

public final class FileUtil {

    private FileUtil() {
    }


    public static Path replaceExtension(Path file, String newExtension) {
        Path parent = file.getParent();
        String name = file.getFileName()
                .toString();
        String nameWithoutExt = extractName(name);
        String newName = nameWithoutExt + newExtension;
        return Optional.ofNullable(parent)
                .map(p -> p.resolve(newName))
                .orElseGet(() -> Path.of(newName));
    }

    private static String extractName(String fileName) {
        Pair<String, String> split = split(fileName);
        String ext = split.getRight();
        if ("part".equalsIgnoreCase(ext)) {
            return extractName(split.getLeft());
        }
        return split.getLeft();
    }

    public static String extractExtension(Path file) {
        String name = file.getFileName()
                .toString();
        return extractExtension(name);
    }

    private static String extractExtension(String name) {
        Pair<String, String> split = split(name);
        String ext = split.getRight();
        if ("part".equalsIgnoreCase(ext)) {
            return extractExtension(split.getLeft());
        }
        return ext;
    }

    public static Pair<String, String> split(String name) {
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return Pair.of(name.substring(0, index), name.substring(index + 1));
        } else {
            return Pair.of(name, null);
        }
    }

}
