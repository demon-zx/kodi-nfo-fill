package ru.java.fun.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.service.model.Season;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = EpisodeMapper.class)
public abstract class SeasonMapper {
    public static SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);

    public abstract Season from(Api.Season source);

}
