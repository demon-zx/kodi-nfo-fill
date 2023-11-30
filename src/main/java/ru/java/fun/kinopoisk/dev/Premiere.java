package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.OffsetDateTime;

public class Premiere {
    private final OffsetDateTime world;
    private final OffsetDateTime russia;

    @JsonCreator
    public Premiere(OffsetDateTime world, OffsetDateTime russia) {
        this.world = world;
        this.russia = russia;
    }

    public OffsetDateTime getWorld() {
        return world;
    }

    public OffsetDateTime getRussia() {
        return russia;
    }
}
