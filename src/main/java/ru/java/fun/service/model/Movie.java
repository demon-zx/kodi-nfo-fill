package ru.java.fun.service.model;

import java.time.OffsetDateTime;
import java.util.List;

public class Movie extends MovieBase {
    private final String originalName;
    private final Status status;
    private final String outline;

    private final String plot;

    private final String tagline;
    private final String poster;
    private final Integer top250;
    private final OffsetDateTime premiere;

    private final List<String> countries;
    private final List<String> genres;
    private final List<String> productions;
    private final List<Person> staff;
    private final List<Rating> ratings;

    public Movie(
            String id,
            String name,
            int year,
            String originalName,
            Status status,
            String outline,
            String plot,
            String tagline,
            String poster,
            Integer top250,
            OffsetDateTime premiere,
            List<String> countries,
            List<String> genres,
            List<String> productions,
            List<Person> staff,
            List<Rating> ratings,
            boolean serial
    ) {
        super(id, name, year, serial);
        this.originalName = originalName;
        this.status = status;
        this.outline = outline;
        this.plot = plot;
        this.tagline = tagline;
        this.poster = poster;
        this.top250 = top250;
        this.premiere = premiere;
        this.countries = countries;
        this.genres = genres;
        this.productions = productions;
        this.staff = staff;
        this.ratings = ratings;
    }

    public String getOriginalName() {
        return originalName;
    }

    public Status getStatus() {
        return status;
    }

    public String getOutline() {
        return outline;
    }

    public String getPlot() {
        return plot;
    }

    public String getTagline() {
        return tagline;
    }

    public String getPoster() {
        return poster;
    }

    public Integer getTop250() {
        return top250;
    }

    public OffsetDateTime getPremiere() {
        return premiere;
    }

    public List<String> getCountries() {
        return countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getProductions() {
        return productions;
    }

    public List<Person> getStaff() {
        return staff;
    }

    public List<Rating> getRatings() {
        return ratings;
    }
}
