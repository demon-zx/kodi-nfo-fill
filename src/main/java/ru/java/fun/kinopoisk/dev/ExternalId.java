package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ExternalId {
    private final String kpHD;
    private final String imdb;
    private final String tmdb;

    @JsonCreator
    public ExternalId(String kpHD, String imdb, String tmdb) {
        this.kpHD = kpHD;
        this.imdb = imdb;
        this.tmdb = tmdb;
    }

    public String getKpHD() {
        return kpHD;
    }

    public String getImdb() {
        return imdb;
    }

    public String getTmdb() {
        return tmdb;
    }
}
