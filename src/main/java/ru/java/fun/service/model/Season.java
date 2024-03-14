package ru.java.fun.service.model;

import java.util.List;

public class Season {
    private final int number;
    private final List<Episode> episodes;

    public Season(int number, List<Episode> episodes) {
        this.number = number;
        this.episodes = episodes;
    }

    public int getNumber() {
        return number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
}
