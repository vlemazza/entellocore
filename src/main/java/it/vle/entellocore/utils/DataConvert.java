package it.vle.entellocore.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DataConvert {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static long toUnixTimestamp(String date) {
        return LocalDateTime.parse(date, DEFAULT_FORMATTER)
                .atZone(ZoneId.of("UTC"))
                .toEpochSecond();
    }

    public static long toUnixTimestampRfc1123(String date) {
        return ZonedDateTime.parse(
                date,
                DateTimeFormatter.RFC_1123_DATE_TIME
        ).toEpochSecond();
    }
}