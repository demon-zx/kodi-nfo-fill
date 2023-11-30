package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ProductionCompany extends Image implements Named {
    private final String name;

    @JsonCreator
    public ProductionCompany(String url, String previewUrl, String name) {
        super(url, previewUrl);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
