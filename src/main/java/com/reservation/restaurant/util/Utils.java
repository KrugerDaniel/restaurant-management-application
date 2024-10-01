package com.reservation.restaurant.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Utils {

    private Utils() {}

    public static String formatTimeZone(final LocalDateTime date) {
        return date.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
    }

    public static String formatDateTime(final LocalDateTime date) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(date);
    }
}
