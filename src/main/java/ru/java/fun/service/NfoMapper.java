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
import java.util.stream.Stream;

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

    public static EpisodeNfo episode(Episode episode) {
        EpisodeNfo nfo = new EpisodeNfo();
        String defaultName = "Эпизод " + episode.getNumber();
        //nfo.setUniqueId();
        String name = Optional.ofNullable(episode.getName())
                .filter(e -> !e.equalsIgnoreCase(defaultName))
                .or(() -> Optional.ofNullable(episode.getEnName()))
                .orElse(defaultName);
        nfo.setTitle(name);
        nfo.setOriginalTitle(episode.getEnName());
        nfo.setPlot(episode.getDescription());
        LocalDate premiered = Optional.ofNullable(episode.getAirDate())
                .map(OffsetDateTime::toLocalDate)
                .orElse(null);
        nfo.setPremiered(premiered);
        return nfo;
    }

    public static TVShowNfo.Status status(Movie.Status status) {
        if (status == null) {
            return TVShowNfo.Status.Continuing;
        }
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
        LocalDate premiered = premiere(movie);
        nfo.setPremiered(premiered);
        if (premiered != null) {
            nfo.setYear(premiered.getYear());
        }
        nfo.setRatings(ratings(movie.getRating(), movie.getVotes()));
        nfo.setTop250(movie.getTop250());
        if (movie.getPoster() != null) {
            nfo.setThumbs(List.of(thumb(ThumbNfo.Aspect.poster, movie.getPoster())));
        }
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
        return Stream.of(
                        Optional.ofNullable(source)
                                .map(s -> {
                                    Integer v = Optional.ofNullable(votes)
                                            .map(Votes::getKp)
                                            .orElse(0);
                                    RatingNfo r = new RatingNfo();
                                    r.setMax(10);
                                    r.setName("kinopoisk");
                                    r.setDefaultValue(true);
                                    r.setValue(s.getKp());
                                    r.setVotes(v);
                                    return r;
                                }),
                        Optional.ofNullable(source)
                                .map(s -> {
                                    Integer v = Optional.ofNullable(votes)
                                            .map(Votes::getImdb)
                                            .orElse(0);
                                    RatingNfo r = new RatingNfo();
                                    r.setMax(10);
                                    r.setName("imdb");
                                    r.setValue(s.getImdb());
                                    r.setVotes(v);
                                    return r;
                                })
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
