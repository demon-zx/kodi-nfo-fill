package ru.java.fun.kinopoisk.dev;

import java.util.List;

public class Movie extends Document {

    private final Videos videos;
    private final ExternalId externalId;
    private final Premiere premiere;
    private final String slogan;
    private final List<Person> persons;
    private final List<ProductionCompany> productionCompanies;

    public Movie(
            int id,
            String name,
            String alternativeName,
            String enName,
            String type,
            int year,
            String description,
            String shortDescription,
            Integer movieLength,
            int ageRating,
            int typeNumber,
            Image logo,
            Image poster,
            Image backdrop,
            Rating rating,
            Votes votes,
            List<ItemName> genres,
            List<ItemName> countries,
            Videos videos,
            ExternalId externalId,
            Premiere premiere,
            String slogan,
            List<Person> persons,
            List<ProductionCompany> productionCompanies
    ) {
        super(
                id,
                name,
                alternativeName,
                enName,
                type,
                year,
                description,
                shortDescription,
                movieLength,
                ageRating,
                typeNumber,
                logo,
                poster,
                backdrop,
                rating,
                votes,
                genres,
                countries
        );
        this.videos = videos;
        this.externalId = externalId;
        this.premiere = premiere;
        this.slogan = slogan;
        this.persons = persons;
        this.productionCompanies = productionCompanies;
    }

    public Videos getVideos() {
        return videos;
    }

    public ExternalId getExternalId() {
        return externalId;
    }

    public Premiere getPremiere() {
        return premiere;
    }

    public String getSlogan() {
        return slogan;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }
}
