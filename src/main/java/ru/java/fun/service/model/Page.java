package ru.java.fun.service.model;

import java.util.List;

public class Page<T> {
    private final int total;
    private final int limit;
    private final int page;
    private final int pages;
    private final List<T> data;

    public Page(int total, int limit, int page, int pages, List<T> data) {
        this.total = total;
        this.limit = limit;
        this.page = page;
        this.pages = pages;
        this.data = data;
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

    public List<T> getData() {
        return data;
    }
}
