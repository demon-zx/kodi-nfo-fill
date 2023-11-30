package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Video {

    private final String url;
    private final String name;
    private final String site;
    private final String type;

    @JsonCreator
    public Video(String url, String name, String site, String type) {
        this.url = url;
        this.name = name;
        this.site = site;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }
}
