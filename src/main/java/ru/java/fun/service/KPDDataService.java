package ru.java.fun.service;

import ru.java.fun.kinopoisk.dev.ApiClient;
import ru.java.fun.service.mapper.MovieMapper;
import ru.java.fun.service.mapper.PageMapper;
import ru.java.fun.service.mapper.SeasonMapper;
import ru.java.fun.service.model.Movie;
import ru.java.fun.service.model.MovieBase;
import ru.java.fun.service.model.Page;
import ru.java.fun.service.model.Season;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class KPDDataService implements DataService {

    private final Logger log;
    private final ApiClient api;

    public KPDDataService(Logger log, ApiClient api) {
        this.log = log;
        this.api = api;
    }

    @Override
    public List<Season> findSeasonsById(String id) throws IOException {
        return api.findSeasonsById(id)
                .stream()
                .map(s -> SeasonMapper.mapper.from(s))
                .collect(Collectors.toList());
    }


    @Override
    public Page<MovieBase> findByQuery(String query, int page, int limit) throws IOException {
        return PageMapper.mapper.from(api.search(query, page, limit));
    }

    @Override
    public Movie findMovieById(String id) throws IOException {
        return MovieMapper.mapper.from(api.findMovieById(id));
    }

    @Override
    public void saveImage(Path path, URI uri) throws IOException {
        api.saveImage(path, uri);
    }

}
