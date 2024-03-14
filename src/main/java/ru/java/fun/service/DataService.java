package ru.java.fun.service;

import ru.java.fun.service.model.Movie;
import ru.java.fun.service.model.MovieBase;
import ru.java.fun.service.model.Page;
import ru.java.fun.service.model.Season;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public interface DataService {


    List<Season> findSeasonsById(String id) throws IOException;

    Page<MovieBase> findByQuery(String query, int page, int limit) throws IOException;

    Movie findMovieById(String id) throws IOException;

    void saveImage(Path path, URI uri) throws IOException;
}
