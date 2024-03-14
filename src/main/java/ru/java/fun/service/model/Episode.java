package ru.java.fun.service.model;

import java.time.OffsetDateTime;

public class Episode {
    private final int number;
    private final String title;
    private final String originalTitle;
    private final String description;
    private final OffsetDateTime premiere;

    public Episode(int number, String title, String originalTitle, String description, OffsetDateTime premiere) {
        this.number = number;
        this.title = title;
        this.originalTitle = originalTitle;
        this.description = description;
        this.premiere = premiere;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getPremiere() {
        return premiere;
    }
}
