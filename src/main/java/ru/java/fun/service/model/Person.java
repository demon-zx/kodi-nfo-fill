package ru.java.fun.service.model;

public class Person {
    private final String name;
    private final String description;
    private final Profession profession;
    private final int order;
    private final String photo;

    public Person(String name, String description, Profession profession, int order, String photo) {
        this.name = name;
        this.description = description;
        this.profession = profession;
        this.order = order;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Profession getProfession() {
        return profession;
    }

    public int getOrder() {
        return order;
    }

    public String getPhoto() {
        return photo;
    }
}
