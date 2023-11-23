package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Votes {
    private final Integer kp;
    private final Integer imdb;
    private final Integer filmCritics;
    private final Integer russianFilmCritics;

    @JsonCreator
    public Votes(Integer kp, Integer imdb, Integer filmCritics, Integer russianFilmCritics) {
        this.kp = kp;
        this.imdb = imdb;
        this.filmCritics = filmCritics;
        this.russianFilmCritics = russianFilmCritics;
    }

    public Integer getKp() {
        return kp;
    }

    public Integer getImdb() {
        return imdb;
    }

    public Integer getFilmCritics() {
        return filmCritics;
    }

    public Integer getRussianFilmCritics() {
        return russianFilmCritics;
    }
}
