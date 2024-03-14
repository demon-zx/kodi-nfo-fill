package ru.java.fun.service.model;

public class MovieBase {

    private final String id;
    private final String name;
    private final int year;
    private final boolean serial;

    public MovieBase(String id, String name, int year, boolean serial) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.serial = serial;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public boolean isSerial() {
        return serial;
    }
}
