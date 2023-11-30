package ru.java.fun.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class FileUtilTest {

    @Test
    void replaceExtension() {
        Path file  = Path.of("/tmp/movie-2023.avi");
        Path expected  = Path.of("/tmp/movie-2023.nfo");
        Path actual = FileUtil.replaceExtension(file, ".nfo");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void appendExtension() {
        Path file  = Path.of("/tmp/movie-2023");
        Path expected  = Path.of("/tmp/movie-2023.nfo");
        Path actual = FileUtil.replaceExtension(file, ".nfo");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void replaceWithoutParentxtension() {
        Path file  = Path.of("movie-2023.avi");
        Path expected  = Path.of("movie-2023.nfo");
        Path actual = FileUtil.replaceExtension(file, ".nfo");
        Assertions.assertEquals(expected, actual);
    }

}