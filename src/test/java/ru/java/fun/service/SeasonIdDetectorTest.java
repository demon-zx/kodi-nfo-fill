package ru.java.fun.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class SeasonIdDetectorTest {

    public static final Optional<Integer> S2E3 = Optional.of(2);

    @Test
    void notDetect() {
        Assertions.assertEquals(Optional.empty(), SeasonIdDetector.detect("ddddd.HD.avi"));
    }

    @Test
    void detectClassic() {
        Assertions.assertEquals(S2E3, SeasonIdDetector.detect("s02"));
        Assertions.assertEquals(S2E3, SeasonIdDetector.detect("S02"));
        Assertions.assertEquals(S2E3, SeasonIdDetector.detect("S2"));
        Assertions.assertEquals(S2E3, SeasonIdDetector.detect("s2"));
    }

}