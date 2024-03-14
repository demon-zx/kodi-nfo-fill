package ru.java.fun.service.model;

public class Rating {
    private final String name;
    private final int max;
    private final boolean defaultValue;
    private final double value;
    private final int votes;


    public Rating(String name, int max, boolean defaultValue, double value, int votes) {
        this.name = name;
        this.max = max;
        this.defaultValue = defaultValue;
        this.value = value;
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public int getMax() {
        return max;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public double getValue() {
        return value;
    }

    public int getVotes() {
        return votes;
    }
}