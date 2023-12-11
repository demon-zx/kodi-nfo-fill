package ru.java.fun.service;

import ru.java.fun.kinopoisk.dev.*;
import ru.java.fun.nfo.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class NfoMapper {

    private NfoMapper(){

    }

    public static TVShowNfo tvShow(Movie movie, List<Season> seasons) {
        TVShowNfo nfo = new TVShowNfo();
        fill(movie, nfo);
        nfo.setSeasonsCount(seasons.size());
        int episodesCount = seasons.stream()
                .mapToInt(Season::getEpisodesCount)
                .sum();
        nfo.setEpisodesCount(episodesCount);
        nfo.setStatus(status(movie.getStatus()));
        return nfo;
    }

    public static TVShowNfo.Status status(Movie.Status status) {
        switch (status){
            case FILMING:
                return TVShowNfo.Status.Continuing;
            case COMPLETED:
                return TVShowNfo.Status.Ended;
            case PRE_PRODUCTION:
            case ANNOUNCED:
            case POST_PRODUCTION:
            default:
                return null;
        }
    }

    public static MovieNfo movie(Movie movie) {
        MovieNfo nfo = new MovieNfo();
        movie.getCountries()
                .stream()
                .findFirst()
                .ifPresent(c -> nfo.setCountry(c.getName()));
        fill(movie, nfo);
        return nfo;
    }

    private static void fill(Movie movie, BaseNfo nfo) {
        nfo.setTitle(movie.getName());
        nfo.setOriginalTitle(Objects.requireNonNullElse(movie.getAlternativeName(), movie.getName()));
        UniqueIdNfo uniqueId = new UniqueIdNfo();
        uniqueId.setType("kp");
        uniqueId.setDefaultValue(true);
        uniqueId.setValue(String.valueOf(movie.getId()));
        nfo.setUniqueId(uniqueId);
        nfo.setOutline(movie.getShortDescription());
        nfo.setPlot(movie.getDescription());
        nfo.setTagline(movie.getSlogan());
        nfo.setPremiered(premiere(movie));
        nfo.setRatings(ratings(movie.getRating(), movie.getVotes()));
        nfo.setTop250(movie.getTop250());
        nfo.setThumbs(List.of(thumb(ThumbNfo.Aspect.poster, movie.getPoster())));
//        movie.setFanArt();
        nfo.setGenre(named(movie.getGenres()));
        nfo.setStudio(named(movie.getProductionCompanies()));
        var personsByProfession = Optional.ofNullable(movie.getPersons())
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Person::getEnProfession));
        nfo.setDirector(named(personsByProfession.get("director")));
        nfo.setCredits(named(personsByProfession.get("writer")));
        nfo.setActors(actors(personsByProfession.get("actor")));
//        movie.setTrailer();
    }

    private static List<ActorNfo> actors(List<Person> actor) {
        AtomicInteger order = new AtomicInteger(0);
        return Optional.ofNullable(actor)
                .stream()
                .flatMap(Collection::stream)
                .map(a -> new ActorNfo(a.getName(), a.getDescription(), order.getAndIncrement(), a.getPhoto()))
                .collect(Collectors.toList());
    }

    private static LocalDate premiere(Movie document) {
        return Optional.ofNullable(document.getPremiere())
                .map(Premiere::getWorld)
                .map(OffsetDateTime::toLocalDate)
                .orElse(null);
    }

    private static List<String> named(List<? extends Named> entries) {
        return Objects.requireNonNullElse(entries, List.<Named>of())
                .stream()
                .map(Named::getName)
                .collect(Collectors.toList());
    }

    public static ThumbNfo thumb(ThumbNfo.Aspect aspect, Image image) {
        ThumbNfo t = new ThumbNfo();
        t.setAspect(aspect);
        t.setPreview(image.getUrl());
        return t;
    }

    public static List<RatingNfo> ratings(ru.java.fun.kinopoisk.dev.Rating source, Votes votes) {
        RatingNfo kp = new RatingNfo();
        kp.setMax(10);
        kp.setName("kinopoisk");
        kp.setDefaultValue(true);
        kp.setValue(source.getKp());
        kp.setVotes(Objects.requireNonNullElse(votes.getKp(), 0));
        RatingNfo imdb = new RatingNfo();
        imdb.setName("imdb");
        imdb.setValue(source.getImdb());
        imdb.setVotes(Objects.requireNonNullElse(votes.getImdb(), 0));
        return List.of(kp, imdb);
    }
}
