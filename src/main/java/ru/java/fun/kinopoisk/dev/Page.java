package ru.java.fun.kinopoisk.dev;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class Page<T> {

    private final int total;
    private final int limit;
    private final int page;
    private final int pages;
    private final List<T> docs;

    @JsonCreator
    public Page(int total, int limit, int page, int pages, List<T> docs) {
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.pages = pages;
        this.docs = docs;
    }

    public int getTotal() {
        return total;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public List<T> getDocs() {
        return docs;
    }
}
