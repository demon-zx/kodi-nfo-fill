package ru.java.fun.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.kinopoisk.dev.Named;
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

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, imports = Objects.class)
public abstract class MovieMapper {
    public static MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    private static List<String> named(List<? extends Named> source) {
        return Objects.requireNonNullElse(source, List.<Named>of())
                .stream()
                .map(Named::getName)
                .collect(Collectors.toList());
    }

    public static List<Rating> ratings(Api.Rating source, Api.Votes votes) {
        return Stream.of(
                        Optional.ofNullable(source)
                                .map(s -> {
                                    Integer v = Optional.ofNullable(votes)
                                            .map(Api.Votes::getKp)
                                            .orElse(0);
                                    return new Rating(
                                            "kinopoisk",
                                            10,
                                            true,
                                            s.getKp(),
                                            v
                                    );
                                }),
                        Optional.ofNullable(source)
                                .map(s -> {
                                    Integer v = Optional.ofNullable(votes)
                                            .map(Api.Votes::getImdb)
                                            .orElse(0);
                                    return new Rating(
                                            "imdb",
                                            10,
                                            false,
                                            s.getImdb(),
                                            v
                                    );
                                })
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public OffsetDateTime premiere(Api.Premiere source) {
        return Optional.ofNullable(source)
                .map(Api.Premiere::getWorld)
                .orElse(null);
    }

    public List<String> companies(List<Api.ProductionCompany> source) {
        return named(source);
    }

    public List<String> items(List<Api.ItemName> source) {
        return named(source);
    }


    public abstract MovieBase from(Api.Document source);

    @ValueMapping(source = "COMPLETED", target = "ENDED")
    @ValueMapping(source = "UNKNOWN", target = MappingConstants.NULL)
    @ValueMapping(source = "FILMING", target = "CONTINUING")
    @ValueMapping(source = "ANNOUNCED", target = "CONTINUING")
    @ValueMapping(source = "POST_PRODUCTION", target = "CONTINUING")
    @ValueMapping(source = "PRE_PRODUCTION", target = "CONTINUING")
    public abstract Status from(Api.Movie.Status source);

    public String from(Api.Image source) {
        return source.getUrl();
    }

    /*
    private static void fill(Movie movie, BaseNfo nfo) {
        nfo.setOriginalTitle(Objects.requireNonNullElse(movie.getOriginalName(), movie.getName()));
        UniqueIdNfo uniqueId = new UniqueIdNfo();
        uniqueId.setType("kp");
        uniqueId.setDefaultValue(true);
        uniqueId.setValue(String.valueOf(movie.getId()));
        nfo.setUniqueId(uniqueId);
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
    }

     */

    @Mapping(target = "staff", source = "persons")
    @Mapping(target = "ratings", expression = "java(ratings(source.getRating(), source.getVotes()))")
    @Mapping(target = "productions", source = "productionCompanies")
    @Mapping(target = "tagline", source = "slogan")
    @Mapping(target = "plot", source = "description")
    @Mapping(target = "outline", source = "shortDescription")
    @Mapping(
            target = "originalName",
            expression = "java(Objects.requireNonNullElse(source.getEnName(), source.getName()))"
    )
    public abstract Movie from(Api.Movie source);

    public Profession profession(Api.Person source) {
        switch (source.getEnProfession()) {
            case "director":
                return Profession.DIRECTOR;
            case "actor":
                return Profession.ACTOR;
            case "writer":
                return Profession.WRITER;
            default:
                return Profession.OTHER;
        }
    }

    public List<Person> persons(List<Api.Person> source) {
        AtomicInteger order = new AtomicInteger(0);
        return Optional.ofNullable(source)
                .stream()
                .flatMap(Collection::stream)
                .map(a -> new Person(
                        a.getName(),
                        a.getDescription(),
                        profession(a),
                        order.getAndIncrement(),
                        a.getPhoto()
                ))
                .collect(Collectors.toList());
    }

}
