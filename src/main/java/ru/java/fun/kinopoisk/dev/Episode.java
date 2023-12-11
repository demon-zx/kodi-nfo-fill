package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.OffsetDateTime;

public class Episode {

    private final int number;
    private final String name;
    private final String enName;
    private final Image still;
    private final int duration;
    private final String description;
    private final OffsetDateTime airDate;
    private final String enDescription;

    @JsonCreator
    public Episode(
            int number,
            String name,
            String enName,
            Image still,
            int duration,
            String description,
            OffsetDateTime airDate,
            String enDescription
    ) {
        this.number = number;
        this.name = name;
        this.enName = enName;
        this.still = still;
        this.duration = duration;
        this.description = description;
        this.airDate = airDate;
        this.enDescription = enDescription;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return enName;
    }

    public Image getStill() {
        return still;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getAirDate() {
        return airDate;
    }

    public String getEnDescription() {
        return enDescription;
    }
}
