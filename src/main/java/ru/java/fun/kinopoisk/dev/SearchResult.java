package ru.java.fun.kinopoisk.dev;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class SearchResult {

    private final int total;
    private final int limit;
    private final int page;
    private final int pages;
    private final List<Document> docs;

    @JsonCreator
    public SearchResult(int total, int limit, int page, int pages, List<Document> docs) {
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

    public List<Document> getDocs() {
        return docs;
    }
}
