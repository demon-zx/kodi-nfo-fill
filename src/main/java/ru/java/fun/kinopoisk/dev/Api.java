package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.util.List;

public final class Api {
    public static class ExternalId {
        private final String kpHD;
        private final String imdb;
        private final String tmdb;

        @JsonCreator
        public ExternalId(String kpHD, String imdb, String tmdb) {
            this.kpHD = kpHD;
            this.imdb = imdb;
            this.tmdb = tmdb;
        }

        public String getKpHD() {
            return kpHD;
        }

        public String getImdb() {
            return imdb;
        }

        public String getTmdb() {
            return tmdb;
        }
    }

    public static class Image {

        private final String url;
        private final String previewUrl;

        @JsonCreator
        public Image(String url, String previewUrl) {
            this.url = url;
            this.previewUrl = previewUrl;
        }

        public String getUrl() {
            return url;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }
    }

    public static class ItemName implements Named {

        private final String name;

        @JsonCreator
        public ItemName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Episode {

        private final int number;
        private final String name;
        private final String enName;
        private final Image still;
        private final int duration;
        private final String description;
        @JsonDeserialize(using = AirDateTimeDeserializer.class)
        private final OffsetDateTime airDate;
        private final String enDescription;

        @JsonCreator
        public Episode(
                int number,
                String name,
                String enName,
                Image still,
                int duration,
                String description,
                OffsetDateTime airDate,
                String enDescription
        ) {
            this.number = number;
            this.name = name;
            this.enName = enName;
            this.still = still;
            this.duration = duration;
            this.description = description;
            this.airDate = airDate;
            this.enDescription = enDescription;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        public String getEnName() {
            return enName;
        }

        public Image getStill() {
            return still;
        }

        public int getDuration() {
            return duration;
        }

        public String getDescription() {
            return description;
        }

        public OffsetDateTime getAirDate() {
            return airDate;
        }

        public String getEnDescription() {
            return enDescription;
        }
    }

    public static class Movie extends Document {
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

    public static class Document {

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
        @JsonProperty("isSeries")
        private final boolean serial;
        private final Integer totalSeriesLength = null;
        private final Integer seriesLength = null;
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
                boolean serial,
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
            this.serial = serial;
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

        public boolean isSerial() {
            return serial;
        }

        public Integer getTotalSeriesLength() {
            return totalSeriesLength;
        }

        public Integer getSeriesLength() {
            return seriesLength;
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

    public static class Page<T> {

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

    public static class Person implements Named {
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

        @JsonCreator
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

    public static class Premiere {
        private final OffsetDateTime world;
        private final OffsetDateTime russia;

        @JsonCreator
        public Premiere(OffsetDateTime world, OffsetDateTime russia) {
            this.world = world;
            this.russia = russia;
        }

        public OffsetDateTime getWorld() {
            return world;
        }

        public OffsetDateTime getRussia() {
            return russia;
        }
    }

    public static class ProductionCompany extends Image implements Named {
        private final String name;

        @JsonCreator
        public ProductionCompany(String url, String previewUrl, String name) {
            super(url, previewUrl);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Rating {
        private final Double kp;
        private final Double imdb;
        private final Double filmCritics;
        private final Double russianFilmCritics;

        @JsonCreator
        public Rating(Double kp, Double imdb, Double filmCritics, Double russianFilmCritics) {
            this.kp = kp;
            this.imdb = imdb;
            this.filmCritics = filmCritics;
            this.russianFilmCritics = russianFilmCritics;
        }

        public Double getKp() {
            return kp;
        }

        public Double getImdb() {
            return imdb;
        }

        public Double getFilmCritics() {
            return filmCritics;
        }

        public Double getRussianFilmCritics() {
            return russianFilmCritics;
        }
    }

    public static class Season {

        private final String id;
        private final int movieId;
        private final int number;
        private final int episodesCount;
        private final List<Episode> episodes;
        private final OffsetDateTime updatedAt;
        @JsonDeserialize(using = AirDateTimeDeserializer.class)
        private final OffsetDateTime airDate;
        private final String description;
        private final int duration;
        private final String enDescription;
        private final String enName;
        private final String name;
        private final Image poster;
        private final String source;

        @JsonCreator
        public Season(
                String id,
                int movieId,
                int number,
                int episodesCount,
                List<Episode> episodes,
                OffsetDateTime updatedAt,
                OffsetDateTime airDate,
                String description,
                int duration,
                String enDescription,
                String enName,
                String name,
                Image poster,
                String source
        ) {
            this.id = id;
            this.movieId = movieId;
            this.number = number;
            this.episodesCount = episodesCount;
            this.episodes = episodes;
            this.updatedAt = updatedAt;
            this.airDate = airDate;
            this.description = description;
            this.duration = duration;
            this.enDescription = enDescription;
            this.enName = enName;
            this.name = name;
            this.poster = poster;
            this.source = source;
        }

        public String getId() {
            return id;
        }

        public int getMovieId() {
            return movieId;
        }

        public int getNumber() {
            return number;
        }

        public int getEpisodesCount() {
            return episodesCount;
        }

        public List<Episode> getEpisodes() {
            return episodes;
        }

        public OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

        public OffsetDateTime getAirDate() {
            return airDate;
        }

        public String getDescription() {
            return description;
        }

        public int getDuration() {
            return duration;
        }

        public String getEnDescription() {
            return enDescription;
        }

        public String getEnName() {
            return enName;
        }

        public String getName() {
            return name;
        }

        public Image getPoster() {
            return poster;
        }

        public String getSource() {
            return source;
        }
    }

    public static class Video {

        private final String url;
        private final String name;
        private final String site;
        private final String type;

        @JsonCreator
        public Video(String url, String name, String site, String type) {
            this.url = url;
            this.name = name;
            this.site = site;
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public String getType() {
            return type;
        }
    }

    public static class Videos {

        private final List<Video> trailers;
        private final List<Video> teasers;

        @JsonCreator
        public Videos(List<Video> trailers, List<Video> teasers) {
            this.trailers = trailers;
            this.teasers = teasers;
        }

        public List<Video> getTrailers() {
            return trailers;
        }

        public List<Video> getTeasers() {
            return teasers;
        }
    }

    public static class Votes {
        private final Integer kp;
        private final Integer imdb;
        private final Integer filmCritics;
        private final Integer russianFilmCritics;

        @JsonCreator
        public Votes(Integer kp, Integer imdb, Integer filmCritics, Integer russianFilmCritics) {
            this.kp = kp;
            this.imdb = imdb;
            this.filmCritics = filmCritics;
            this.russianFilmCritics = russianFilmCritics;
        }

        public Integer getKp() {
            return kp;
        }

        public Integer getImdb() {
            return imdb;
        }

        public Integer getFilmCritics() {
            return filmCritics;
        }

        public Integer getRussianFilmCritics() {
            return russianFilmCritics;
        }
    }
}
