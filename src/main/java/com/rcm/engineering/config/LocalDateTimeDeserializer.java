package com.rcm.engineering.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter ISO_WITH_MILLIS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DateTimeFormatter DB_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    private static final DateTimeFormatter DB_NO_FRACTION =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String value = p.getText();
        try {
            return LocalDateTime.parse(value, ISO_FORMAT);
        } catch (Exception e1) {
            try {
                return LocalDateTime.parse(value, ISO_WITH_MILLIS);
            } catch (Exception e2) {
                try {
                    return LocalDateTime.parse(value, DB_FORMAT);
                } catch (Exception e3) {
                    return LocalDateTime.parse(value, DB_NO_FRACTION);
                }
            }
        }
    }
}