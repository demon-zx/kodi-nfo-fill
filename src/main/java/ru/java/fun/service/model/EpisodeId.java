package ru.java.fun.service.model;

import java.util.Objects;

public class EpisodeId {

    private final int season;
    private final int episode;

    public EpisodeId(int season, int episode) {
        this.season = season;
        this.episode = episode;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeId episodeId = (EpisodeId) o;
        return season == episodeId.season && episode == episodeId.episode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(season, episode);
    }

    @Override
    public String toString() {
        return "S" + season +
                "E" + episode;
    }
}
