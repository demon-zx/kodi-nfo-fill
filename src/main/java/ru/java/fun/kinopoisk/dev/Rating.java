package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Rating {
    private final Double kp;
    private final Double imdb;
    private final Double filmCritics;
    private final Double russianFilmCritics;

    @JsonCreator
    public Rating(Double kp, Double imdb, Double filmCritics, Double russianFilmCritics) {
        this.kp = kp;
        this.imdb = imdb;
        this.filmCritics = filmCritics;
        this.russianFilmCritics = russianFilmCritics;
    }

    public Double getKp() {
        return kp;
    }

    public Double getImdb() {
        return imdb;
    }

    public Double getFilmCritics() {
        return filmCritics;
    }

    public Double getRussianFilmCritics() {
        return russianFilmCritics;
    }
}
