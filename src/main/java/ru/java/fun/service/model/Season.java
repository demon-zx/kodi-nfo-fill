package ru.java.fun.service.model;

import java.util.List;
import java.util.Objects;

public class Season {
    private final int number;
    private final List<Episode> episodes;

    public Season(int number, List<Episode> episodes) {
        this.number = number;
        this.episodes = Objects.requireNonNullElse(episodes, List.of());
    }

    public int getNumber() {
        return number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
}
