package ru.java.fun.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ru.java.fun.service.model.Season;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class TSVLoaderTest {

    @Test
    void testLoad() throws IOException {
        StringReader reader = new StringReader(
            "Episode id\tEpisode name\tOriginal episode name\tPremiere yyyy.MM.dd\tDescription\r\n" +
            "s2e3\t\"4,5\"\t\"6. 8\"\t2018.08.17\t\"90\"\r\n"
        );
        List<Season> actual = TSVLoader.load(reader);
        var actualSeason = actual.get(0);
        Assertions.assertEquals(2, actualSeason.getNumber());
        var actualEpisode = actualSeason.getEpisodes()
            .get(0);
        Assertions.assertEquals(3, actualEpisode.getNumber());
        Assertions.assertEquals("4,5", actualEpisode.getTitle());
        Assertions.assertEquals("6. 8", actualEpisode.getOriginalTitle());
        Assertions.assertEquals("90", actualEpisode.getDescription());
        OffsetDateTime expectedPremiere = LocalDateTime.parse("2018-08-17T00:00:00")
            .atZone(ZoneId.systemDefault())
            .toOffsetDateTime();
        Assertions.assertEquals(expectedPremiere, actualEpisode.getPremiere());
    }

}
