package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Image {

    private final String url;
    private final String previewUrl;

    @JsonCreator
    public Image(String url, String previewUrl) {
        this.url = url;
        this.previewUrl = previewUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }
}
