package ru.java.fun.util;

import java.nio.file.Path;
import java.util.Optional;

public final class FileUtil {

    private FileUtil() {
    }


    public static Path replaceExtension(Path file, String newExtension) {
        Path parent = file.getParent();
        String name = file.getFileName()
                .toString();
        int index = name.lastIndexOf('.');
        String nameWithoutExt;
        if (index > 0) {
            nameWithoutExt = name.substring(0, index);
        } else {
            nameWithoutExt = name;
        }
        String newName = nameWithoutExt + newExtension;
        return Optional.ofNullable(parent)
                .map(p -> p.resolve(newName))
                .orElseGet(() -> Path.of(newName));
    }

}
