package ru.java.fun.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.service.model.Episode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EpisodeMapper {
    public static EpisodeMapper mapper = Mappers.getMapper(EpisodeMapper.class);

    @Mapping(target = "premiere", source = "airDate")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "originalTitle", source = "enName")
    public abstract Episode from(Api.Episode source);

    public List<Episode> from(List<Api.Episode> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map(this::from)
                .sorted(Comparator.comparing(Episode::getNumber))
                .collect(Collectors.toList());
    }

}
