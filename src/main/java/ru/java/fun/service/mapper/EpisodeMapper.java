package ru.java.fun.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.service.model.Episode;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class EpisodeMapper {
    public static EpisodeMapper mapper = Mappers.getMapper(EpisodeMapper.class);

    @Mapping(target = "premiere", source = "airDate")
    @Mapping(target = "title", source="name")
    @Mapping(target = "originalTitle", source="enName")
    public abstract Episode from(Api.Episode source);

}
