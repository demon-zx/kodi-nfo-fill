package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.OffsetDateTime;
import java.util.List;

public class Season {

    private final String id;
    private final int movieId;
    private final int number;
    private final int episodesCount;
    private final List<Episode> episodes;
    private final OffsetDateTime updatedAt;
    private final OffsetDateTime airDate;
    private final String description;
    private final int duration;
    private final String enDescription;
    private final String enName;
    private final String name;
    private final Image poster;
    private final String source;

    @JsonCreator
    public Season(
            String id,
            int movieId,
            int number,
            int episodesCount,
            List<Episode> episodes,
            OffsetDateTime updatedAt,
            OffsetDateTime airDate,
            String description,
            int duration,
            String enDescription,
            String enName,
            String name,
            Image poster,
            String source
    ) {
        this.id = id;
        this.movieId = movieId;
        this.number = number;
        this.episodesCount = episodesCount;
        this.episodes = episodes;
        this.updatedAt = updatedAt;
        this.airDate = airDate;
        this.description = description;
        this.duration = duration;
        this.enDescription = enDescription;
        this.enName = enName;
        this.name = name;
        this.poster = poster;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getNumber() {
        return number;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getAirDate() {
        return airDate;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getEnDescription() {
        return enDescription;
    }

    public String getEnName() {
        return enName;
    }

    public String getName() {
        return name;
    }

    public Image getPoster() {
        return poster;
    }

    public String getSource() {
        return source;
    }
}
