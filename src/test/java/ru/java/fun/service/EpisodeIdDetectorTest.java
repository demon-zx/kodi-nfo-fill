package ru.java.fun.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EpisodeIdDetectorTest {

    @Test
    void detectClassic() {
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/s02e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/serial.s02e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/serial.s02.e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/S02E03.avi")));
    }

    @Test
    void detectSimpleNumber() {
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/s02/e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/s02/3.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/S2/03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/S02/E3.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/02/e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/02/3.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/2/03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(2, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/02/E3.avi")));
        Assertions.assertEquals(new EpisodeId(1, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/e03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(1, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/3.avi")));
        Assertions.assertEquals(new EpisodeId(1, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/03.HD.avi")));
        Assertions.assertEquals(new EpisodeId(1, 3), EpisodeIdDetector.detect(Path.of("d:/films/serial/E3.avi")));
    }
}