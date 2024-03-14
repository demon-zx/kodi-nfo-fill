package ru.java.fun.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.java.fun.service.model.EpisodeId;

import java.nio.file.Path;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class EpisodeIdDetectorTest {

    public static final Optional<EpisodeId> S2E3 = Optional.of(new EpisodeId(2, 3));
    public static final Optional<EpisodeId> S5E1 = Optional.of(new EpisodeId(1, 3));

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

}