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

public final class NfoGenerator {

    private NfoGenerator(){

    }

    public static MovieNfo movie(Movie document) {
        MovieNfo movie = new MovieNfo();
        movie.setTitle(document.getName());
        movie.setOriginalTitle(Objects.requireNonNullElse(document.getAlternativeName(), document.getName()));
        document.getCountries()
                .stream()
                .findFirst()
                .ifPresent(c -> movie.setCountry(c.getName()));
        UniqueIdNfo uniqueId = new UniqueIdNfo();
        uniqueId.setType("kp");
        uniqueId.setDefaultValue(true);
        uniqueId.setValue(String.valueOf(document.getId()));
        movie.setUniqueId(uniqueId);
        movie.setOutline(document.getShortDescription());
        movie.setPlot(document.getDescription());
        movie.setTagline(document.getSlogan());
        movie.setPremiered(premiere(document));
        movie.setRatings(ratings(document.getRating(), document.getVotes()));
        movie.setTop250(document.getTop250());
        movie.setThumbs(List.of(thumb(ThumbNfo.Aspect.poster, document.getPoster())));
//        movie.setFanArt();
        movie.setGenre(named(document.getGenres()));
        movie.setStudio(named(document.getProductionCompanies()));
        var personsByProfession = Optional.ofNullable(document.getPersons())
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Person::getEnProfession));
        movie.setDirector(named(personsByProfession.get("director")));
        movie.setCredits(named(personsByProfession.get("writer")));
        movie.setActors(actors(personsByProfession.get("actor")));
//        movie.setTrailer();

        return movie;
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
