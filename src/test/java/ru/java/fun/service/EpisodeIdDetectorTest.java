package ru.java.fun.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class EpisodeIdDetectorTest {

    public static final Optional<EpisodeId> S2E3 = Optional.of(new EpisodeId(2, 3));
    public static final Optional<EpisodeId> S1E3 = Optional.of(new EpisodeId(1, 3));

    @Test
    void notDetect() {
        Assertions.assertEquals(Optional.empty(), EpisodeIdDetector.detect(Path.of("d:/films/serial/ddddd.HD.avi")));
    }

    @Test
    void detectClassic() {
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/s02e03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/serial.s02e03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/serial.s02.e03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/S02E03.avi")));
    }

    @Test
    void detectCyrillic() {
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/сезон №2 серия №3.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/сезон_2_серия_3.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/СЕЗОН 2 СЕРИЯ 3.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/Сезон#2Серия№3.avi")));
    }


    @Test
    void detectSimpleNumber() {
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/s02/e03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/s02/3.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/S2/03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/S02/E3.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/02/e03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/02/3.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/2/03.HD.avi")));
        Assertions.assertEquals(S2E3, EpisodeIdDetector.detect(Path.of("d:/films/serial/02/E3.avi")));
        Assertions.assertEquals(Optional.empty(), EpisodeIdDetector.detect(Path.of("d:/films/serial/e03.HD.avi")));
        Assertions.assertEquals(Optional.empty(), EpisodeIdDetector.detect(Path.of("d:/films/serial/3.avi")));
        Assertions.assertEquals(Optional.empty(), EpisodeIdDetector.detect(Path.of("d:/films/serial/03.HD.avi")));
        Assertions.assertEquals(Optional.empty(), EpisodeIdDetector.detect(Path.of("d:/films/serial/E3.avi")));
    }
}