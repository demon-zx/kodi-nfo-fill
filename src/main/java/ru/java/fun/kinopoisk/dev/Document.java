package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class Document {

    private final int id;
    private final String name;
    /**
     * Original?
     */
    private final String alternativeName;
    private final String enName;
    private final String type;
    private final int year;
    private final String description;
    private final String shortDescription;
    private final Integer movieLength;
    //    private final boolean isSeries;
    private final Integer totalSeriesLength = null;
    private final Integer seriesLength = null;
    private final String ratingMpaa = "r";
    private final int ageRating;
    private final Integer top10 = null;
    private final Integer top250 = null;
    private final int typeNumber;
//    private final Object names;

    private final Image logo;
    private final Image poster;
    private final Image backdrop;
    private final Rating rating;
    private final Votes votes;
    private final List<ItemName> genres;
    private final List<ItemName> countries;

    @JsonCreator
    public Document(
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
            List<ItemName> countries
    ) {
        this.id = id;
        this.name = name;
        this.alternativeName = alternativeName;
        this.enName = enName;
        this.type = type;
        this.year = year;
        this.description = description;
        this.shortDescription = shortDescription;
        this.movieLength = movieLength;
        this.ageRating = ageRating;
        this.typeNumber = typeNumber;
        this.logo = logo;
        this.poster = poster;
        this.backdrop = backdrop;
        this.rating = rating;
        this.votes = votes;
        this.genres = genres;
        this.countries = countries;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public String getEnName() {
        return enName;
    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public Integer getMovieLength() {
        return movieLength;
    }

    public Integer getTotalSeriesLength() {
        return totalSeriesLength;
    }

    public Integer getSeriesLength() {
        return seriesLength;
    }

    public String getRatingMpaa() {
        return ratingMpaa;
    }

    public int getAgeRating() {
        return ageRating;
    }

    public Integer getTop10() {
        return top10;
    }

    public Integer getTop250() {
        return top250;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public Image getLogo() {
        return logo;
    }

    public Image getPoster() {
        return poster;
    }

    public Image getBackdrop() {
        return backdrop;
    }

    public Rating getRating() {
        return rating;
    }

    public Votes getVotes() {
        return votes;
    }

    public List<ItemName> getGenres() {
        return genres;
    }

    public List<ItemName> getCountries() {
        return countries;
    }
}
