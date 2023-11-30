package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class Videos {

    private final List<Video> trailers;
    private final List<Video> teasers;

    @JsonCreator
    public Videos(List<Video> trailers, List<Video> teasers) {
        this.trailers = trailers;
        this.teasers = teasers;
    }

    public List<Video> getTrailers() {
        return trailers;
    }

    public List<Video> getTeasers() {
        return teasers;
    }
}
