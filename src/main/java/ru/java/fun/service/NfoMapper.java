package ru.java.fun.service;

import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.nfo.*;
import ru.java.fun.service.model.*;

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

    private NfoMapper() {
    }

    public static TVShowNfo tvShow(Movie movie, List<Season> seasons) {
        TVShowNfo nfo = new TVShowNfo();
        fill(movie, nfo);
        nfo.setSeasonsCount(seasons.size());
        int episodesCount = seasons.stream()
                .map(Season::getEpisodes)
                .mapToInt(List::size)
                .sum();
        nfo.setEpisodesCount(episodesCount);
        nfo.setStatus(status(movie.getStatus()));
        return nfo;
    }

    public static EpisodeNfo episode(Episode episode) {
        EpisodeNfo nfo = new EpisodeNfo();
        String defaultName = "Эпизод " + episode.getNumber();
        //nfo.setUniqueId();
        String name = Optional.ofNullable(episode.getTitle())
                .filter(e -> !e.equalsIgnoreCase(defaultName))
                .or(() -> Optional.ofNullable(episode.getOriginalTitle()))
                .orElse(defaultName);
        nfo.setTitle(name);
        nfo.setOriginalTitle(episode.getOriginalTitle());
        nfo.setPlot(episode.getDescription());
        LocalDate premiered = Optional.ofNullable(episode.getPremiere())
                .map(OffsetDateTime::toLocalDate)
                .orElse(null);
        nfo.setPremiered(premiered);
        return nfo;
    }

    public static TVShowNfo.Status status(Status status) {
        if (status == null) {
            return TVShowNfo.Status.Continuing;
        }
        switch (status) {
            case CONTINUING:
                return TVShowNfo.Status.Continuing;
            case ENDED:
                return TVShowNfo.Status.Ended;
            default:
                throw new IllegalArgumentException("Unknown value: " + status);
        }
    }

    public static MovieNfo movie(Movie movie) {
        MovieNfo nfo = new MovieNfo();
        movie.getCountries()
                .stream()
                .findFirst()
                .ifPresent(nfo::setCountry);
        fill(movie, nfo);
        return nfo;
    }

    private static void fill(Movie movie, BaseNfo nfo) {
        nfo.setTitle(movie.getName());
        nfo.setOriginalTitle(Objects.requireNonNullElse(movie.getOriginalName(), movie.getName()));
        UniqueIdNfo uniqueId = new UniqueIdNfo();
        uniqueId.setType("kp");
        uniqueId.setDefaultValue(true);
        uniqueId.setValue(String.valueOf(movie.getId()));
        nfo.setUniqueId(uniqueId);
        nfo.setOutline(movie.getOutline());
        nfo.setPlot(movie.getPlot());
        nfo.setTagline(movie.getTagline());
        Optional.ofNullable(movie.getPremiere())
                .map(OffsetDateTime::toLocalDate)
                .ifPresent(p -> {
                    nfo.setPremiered(p);
                    nfo.setYear(p.getYear());
                });
        var ratings = Optional.ofNullable(movie.getRatings())
                .stream()
                .flatMap(Collection::stream)
                .map(NfoMapper::rating)
                .collect(Collectors.toList());
        nfo.setRatings(ratings);
        nfo.setTop250(movie.getTop250());
        if (movie.getPoster() != null) {
            nfo.setThumbs(List.of(thumb(ThumbNfo.Aspect.poster, movie.getPoster())));
        }
        nfo.setGenre(movie.getGenres());
        nfo.setStudio(movie.getProductions());
        var personsByProfession = Optional.ofNullable(movie.getStaff())
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Person::getProfession));
        nfo.setDirector(names(personsByProfession.get(Profession.DIRECTOR)));
        nfo.setCredits(names(personsByProfession.get(Profession.WRITER)));
        nfo.setActors(actors(personsByProfession.get(Profession.ACTOR)));
    }

    private static List<ActorNfo> actors(List<Person> actor) {
        AtomicInteger order = new AtomicInteger(0);
        return Optional.ofNullable(actor)
                .stream()
                .flatMap(Collection::stream)
                .map(a -> new ActorNfo(a.getName(), a.getDescription(), order.getAndIncrement(), a.getPhoto()))
                .collect(Collectors.toList());
    }

    private static List<String> names(List<Person> entries) {
        return Optional.ofNullable(entries)
                .stream()
                .flatMap(Collection::stream)
                .map(Person::getName)
                .collect(Collectors.toList());
    }

    public static ThumbNfo thumb(ThumbNfo.Aspect aspect, String url) {
        ThumbNfo t = new ThumbNfo();
        t.setAspect(aspect);
        t.setPreview(url);
        return t;
    }

    public static RatingNfo rating(Rating rating) {
        var r = new RatingNfo();
        r.setName(rating.getName());
        r.setMax(rating.getMax());
        r.setDefaultValue(rating.isDefaultValue());
        r.setValue(rating.getValue());
        r.setVotes(rating.getVotes());
        return r;
    }


}
