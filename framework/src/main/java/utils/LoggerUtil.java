package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standardized logging utility for test execution and framework events.
 */
public final class LoggerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);
    private static final String SEPARATOR = "================================================================================";

    private LoggerUtil() {
        // Prevent instantiation
    }

    public static void info(String message, Object... params) {
        LOGGER.info(message, params);
    }

    public static void warn(String message, Object... params) {
        LOGGER.warn(message, params);
    }

    public static void error(String message, Object... params) {
        LOGGER.error(message, params);
    }

    public static void debug(String message, Object... params) {
        LOGGER.debug(message, params);
    }

    /**
     * Logs a test step in a highlighted visual format.
     */
    public static void step(String stepDescription) {
        LOGGER.info(">>> STEP: {}", stepDescription);
    }

    /**
     * Logs the start of a test case with clean dividers.
     */
    public static void startTestCase(String testName) {
        LOGGER.info("\n{}\n  STARTING TEST: {}\n{}", SEPARATOR, testName, SEPARATOR);
    }

    /**
     * Logs the completion of a test case.
     */
    public static void endTestCase(String testName, String result) {
        LOGGER.info("\n{}\n  COMPLETED TEST: {} | STATUS: {}\n{}\n", SEPARATOR, testName, result, SEPARATOR);
    }

    /**
     * Logs a distinct execution section.
     */
    public static void section(String sectionName) {
        LOGGER.info("\n--- [ {} ] ---", sectionName);
    }
}
