package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class Movie extends Document {
    private final Status status;

    private final Videos videos;
    private final ExternalId externalId;
    private final Premiere premiere;
    private final String slogan;
    private final List<Person> persons;
    private final List<ProductionCompany> productionCompanies;

    @JsonCreator
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
            boolean serial,
            int ageRating,
            int typeNumber,
            Image logo,
            Image poster,
            Image backdrop,
            Rating rating,
            Votes votes,
            List<ItemName> genres,
            List<ItemName> countries,
            Status status,
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
                serial,
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
        this.status = status;
        this.videos = videos;
        this.externalId = externalId;
        this.premiere = premiere;
        this.slogan = slogan;
        this.persons = persons;
        this.productionCompanies = productionCompanies;
    }

    public Status getStatus() {
        return status;
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


    public enum Status {
        @JsonEnumDefaultValue
        UNKNOWN("unknown"),
        FILMING("filming"),
        PRE_PRODUCTION("pre-production"),
        COMPLETED("completed"),
        ANNOUNCED("announced"),
        POST_PRODUCTION("post-production");

        private final String serialized;

        Status(String serialized) {
            this.serialized = serialized;
        }

        @JsonValue
        public String serialized() {
            return serialized;
        }
    }
}
