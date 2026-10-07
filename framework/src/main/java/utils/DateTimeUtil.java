package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Reusable utility for formatting dates, timestamps, and test execution timings.
 */
public final class DateTimeUtil {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateTimeUtil() {
        // Prevent instantiation
    }

    /**
     * Returns the current timestamp suitable for filenames (e.g. 20261005_123000).
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * Returns human-readable current date and time for reporting (e.g. 2026-10-05 12:30:00).
     */
    public static String getDisplayDateTime() {
        return LocalDateTime.now().format(DISPLAY_FORMATTER);
    }
}
