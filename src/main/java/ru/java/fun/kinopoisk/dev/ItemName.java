package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ItemName {

    private final String name;

    @JsonCreator
    public ItemName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
