package ru.java.fun.command;

import ru.java.fun.kinopoisk.dev.Document;
import ru.java.fun.kinopoisk.dev.Image;
import ru.java.fun.kinopoisk.dev.ItemName;
import ru.java.fun.kinopoisk.dev.Votes;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.Rating;
import ru.java.fun.nfo.Thumb;
import ru.java.fun.nfo.UniqueId;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class NfoGenerator {

    private NfoGenerator(){

    }

    public static MovieNfo movie(Document document) {
        MovieNfo movie = new MovieNfo();
        movie.setTitle(document.getName());
        movie.setOriginalTitle(Objects.requireNonNullElse(document.getAlternativeName(), document.getName()));
        document.getCountries()
                .stream()
                .findFirst()
                .ifPresent(c -> movie.setCountry(c.getName()));
        UniqueId uniqueId = new UniqueId();
        uniqueId.setType("kp");
        uniqueId.setDefaultValue(true);
        uniqueId.setValue(String.valueOf(document.getId()));
        movie.setUniqueId(uniqueId);
        movie.setOutline(document.getShortDescription());
        movie.setPlot(document.getDescription());
        movie.setPremiered(LocalDate.of(document.getYear(),1,1));
        movie.setRatings(ratings(document.getRating(), document.getVotes()));
        movie.setTop250(document.getTop250());
        movie.setThumbs(List.of(thumb(Thumb.Aspect.poster, document.getPoster())));
//        movie.setFanArt();
        List<String> genres = document.getGenres()
                .stream()
                .map(ItemName::getName)
                .collect(Collectors.toList());
        movie.setGenre(genres);
//        movie.setCredits();
//        movie.setDirector();
//        movie.setStudio();
//        movie.setTrailer();
//        movie.setStars();

        return movie;
    }

    public static Thumb thumb(Thumb.Aspect aspect, Image image) {
        Thumb t = new Thumb();
        t.setAspect(aspect);
        t.setPreview(image.getUrl());
        return t;
    }

    public static List<Rating> ratings(ru.java.fun.kinopoisk.dev.Rating source, Votes votes) {
        Rating kp = new Rating();
        kp.setMax(10);
        kp.setName("kinopoisk");
        kp.setDefaultValue(true);
        kp.setValue(source.getKp());
        kp.setVotes(Objects.requireNonNullElse(votes.getKp(), 0));
        Rating imdb = new Rating();
        imdb.setName("imdb");
        imdb.setValue(source.getImdb());
        imdb.setVotes(Objects.requireNonNullElse(votes.getImdb(), 0));
        return List.of(kp, imdb);
    }
}
