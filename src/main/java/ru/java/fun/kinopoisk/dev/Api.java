package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.function.FailableFunction;
import ru.java.fun.service.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public class Api {
    private static final String CONTENT_TYPE = "application/json";
    private static final int PAGE_SIZE = 25;

    private final String uri;
    private final String token;
    private final Logger log;
    private final Function<Reader, Reader> responseWrapper;

    private final Duration readTimeout;

    private final ObjectMapper mapper;
    private final HttpClient httpClient;
    private final TypeReference<Page<Document>> TR_SEARCH = new TypeReference<>() {
    };
    private final TypeReference<Page<Season>> TR_SEASONS = new TypeReference<>() {
    };

    public Api(
            String uri,
            String token,
            Logger log,
            Function<Reader, Reader> responseWrapper,
            Duration connectionTimeout,
            Duration readTimeout
    ) {
        this.uri = uri.endsWith("/") ? uri : uri + "/";
        this.token = token;
        this.log = log;
        this.responseWrapper = Objects.requireNonNullElseGet(responseWrapper, () -> r -> r);
        this.readTimeout = readTimeout;
        mapper = new ObjectMapper();
        mapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        mapper.registerModule(new JavaTimeModule());
        httpClient = HttpClient.newBuilder()
                .connectTimeout(connectionTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    static <T> List<T> all(FailableFunction<Integer, Page<T>, IOException> function) throws IOException {
        int number = 0;
        int all = 1;
        List<T> result = new ArrayList<>();
        while (number < all) {
            Page<T> page = function.apply(++number);
            result.addAll(page.getDocs());
            all = page.getPages();
        }
        return result;
    }

    public Page<Document> search(String name, int page, int limit) throws IOException {
        String resource = "movie/search?" + buildQuery(Map.of(
                "query", name,
                "page", page,
                "limit", limit
        ));        return request(resource, TR_SEARCH);
    }

    public Movie findMovieById(long id) throws IOException {
        String resource = "movie/" + id;
        return request(resource, Movie.class);
    }

    public List<Season> findSeasonsById(long id) throws IOException {
        return all(page -> findSeasonsById(id, page, PAGE_SIZE));
    }

    public Page<Season> findSeasonsById(long id, int page, int limit) throws IOException {
        String resource = "season?" + buildQuery(Map.of(
                "movieId", id,
                "page", page,
                "limit", limit
        ));
        return request(resource, TR_SEASONS);
    }

    String buildQuery(Map<String, ?> queryParameters) {
        StringBuilder sb = new StringBuilder(" ");
        for (Map.Entry<String, ?> e : queryParameters.entrySet()) {
            Object value = e.getValue();
            if (value instanceof String) {
                value = URLEncoder.encode((String) value, StandardCharsets.UTF_8);
            }
            sb.append(e.getKey())
                    .append("=")
                    .append(value)
                    .append("&");
        }
        return sb.substring(0, sb.length() - 1)
                .trim();
    }

    public <T> T request(
            String resource,
            Class<T> responseClazz
    ) throws IOException {
        return request(resource, new TypeReference<>() {
            @Override
            public Type getType() {
                return responseClazz;
            }
        });
    }

    public <T> T request(
            String resource,
            TypeReference<T> responseClazz
    ) throws IOException {
        log.println(Logger.Level.DEBUG, ">>> GET " + resource);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(uri + resource))
                .header("X-API-KEY", token)
                .header("Content-Type", CONTENT_TYPE)
                .GET()
                .timeout(readTimeout)
                .build();
        HttpResponse<InputStream> response;
        try {
            response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofInputStream()
            );
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
        if (response.statusCode() == 200) {
            try (Reader reader = reader(response)) {
                boolean json = response.headers()
                        .firstValue("Content-Type")
                        .filter(v -> v.startsWith("application/json"))
                        .isPresent();
                if(json) {
                    return mapper.readValue(reader, responseClazz);
                } else {
                    StringWriter sw = new StringWriter();
                    IOUtils.copy(reader, sw);
                    throw new IOException("Server return:\n" + sw);
                }
            }
        }
        throw new IOException(String.format(
                "%s: %s %s",
                response.statusCode(),
                request.method(),
                request.uri()
        ));
    }

    private Reader reader(HttpResponse<InputStream> response) {
        Reader reader = new InputStreamReader(response.body(), StandardCharsets.UTF_8);
        reader = responseWrapper.apply(reader);
        return reader;
    }

    public void saveImage(Path target, URI source) throws IOException {
        Files.deleteIfExists(target);
        try (OutputStream os = Files.newOutputStream(target, WRITE, CREATE_NEW)) {
            var request = HttpRequest.newBuilder()
                    .uri(source)
                    .GET()
                    .timeout(readTimeout)
                    .build();
            HttpResponse<InputStream> response;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            if (response.statusCode() == 200) {
                IOUtils.copy(response.body(), os);
                os.flush();
                return;
            }
            throw new IOException(String.format(
                    "%s: %s %s",
                    response.statusCode(),
                    request.method(),
                    request.uri()
            ));
        }
    }
}
