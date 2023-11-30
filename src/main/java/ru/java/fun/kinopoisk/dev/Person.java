package ru.java.fun.kinopoisk.dev;

public class Person implements Named {
    private final long id;
    private final String photo;
    private final String name;
    private final String enName;
    private final String description;
    private final String profession;
    private final String enProfession;

    public long getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return enName;
    }

    public String getDescription() {
        return description;
    }

    public String getProfession() {
        return profession;
    }

    public String getEnProfession() {
        return enProfession;
    }

    public Person(
            long id,
            String photo,
            String name,
            String enName,
            String description,
            String profession,
            String enProfession
    ) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.enName = enName;
        this.description = description;
        this.profession = profession;
        this.enProfession = enProfession;


    }
}
