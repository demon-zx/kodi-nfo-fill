package ru.java.fun.kinopoisk.dev;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AirDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    private final DateTimeFormatter FULL = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final DateTimeFormatter SHORT = DateTimeFormatter.ISO_DATE;

    @Override
    public OffsetDateTime deserialize(
            JsonParser p,
            DeserializationContext ctxt
    ) throws IOException {
        String text = p.getText();
        if (text.length() == 10) {
            LocalDate parsed = LocalDate.parse(text, SHORT);
            return parsed.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime();
        }
        return OffsetDateTime.parse(text, FULL);
    }
}
