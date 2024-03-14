package ru.java.fun.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.java.fun.kinopoisk.dev.Api;
import ru.java.fun.service.model.MovieBase;
import ru.java.fun.service.model.Page;
import ru.java.fun.service.model.Season;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {
        EpisodeMapper.class,
        MovieMapper.class
})
public abstract class PageMapper {
    public static PageMapper mapper = Mappers.getMapper(PageMapper.class);

    @Mapping(target = "data", source = "docs")
    public abstract Page<MovieBase> from(Api.Page<Api.Document> source);

}
