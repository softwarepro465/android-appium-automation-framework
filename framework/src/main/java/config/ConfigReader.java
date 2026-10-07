package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reusable configuration reader supporting hierarchical overrides:
 * System Properties -> Environment Variables -> App Properties -> Framework Defaults.
 */
public class ConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties FRAMEWORK_PROPS = new Properties();
    private static final Properties APP_PROPS = new Properties();

    static {
        loadFrameworkDefaults();
    }

    private ConfigReader() {
        // Utility class
    }

    /**
     * Loads the default framework configuration from classpath.
     */
    private static void loadFrameworkDefaults() {
        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream("framework.properties")) {
            if (stream != null) {
                FRAMEWORK_PROPS.load(stream);
                LOGGER.info("Loaded default framework.properties from classpath.");
            } else {
                LOGGER.warn("framework.properties not found on classpath.");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load framework.properties: {}", e.getMessage(), e);
        }
    }

    /**
     * Loads application-specific properties from a file path.
     * Allows app-level overrides without modifying framework files.
     *
     * @param filePath Path to the app properties file
     */
    public static synchronized void loadAppProperties(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try (InputStream stream = new FileInputStream(file)) {
                APP_PROPS.load(stream);
                LOGGER.info("Successfully loaded application properties from: {}", filePath);
            } catch (IOException e) {
                LOGGER.error("Error reading application properties file [{}]: {}", filePath, e.getMessage(), e);
            }
        } else {
            LOGGER.warn("Application properties file not found at: {}", filePath);
        }
    }

    /**
     * Retrieves a configuration property using hierarchical priority.
     *
     * @param key          Configuration key
     * @param defaultValue Default value if not set
     * @return Resolved property value
     */
    public static String get(String key, String defaultValue) {
        // 1. Check System Property
        String sysVal = System.getProperty(key);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }

        // 2. Check Environment Variable (e.g. appium.server.url -> APPIUM_SERVER_URL)
        String envKey = key.replace('.', '_').toUpperCase();
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }

        // 3. Check App-specific Properties
        String appVal = APP_PROPS.getProperty(key);
        if (appVal != null && !appVal.trim().isEmpty()) {
            return appVal.trim();
        }

        // 4. Check Framework Default Properties
        String fwVal = FRAMEWORK_PROPS.getProperty(key);
        if (fwVal != null && !fwVal.trim().isEmpty()) {
            return fwVal.trim();
        }

        return defaultValue;
    }

    /**
     * Retrieves a configuration property or null if not found.
     *
     * @param key Configuration key
     * @return Resolved property value or null
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * Retrieves an integer property value.
     */
    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid integer value '{}' for key '{}', using default: {}", val, key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Retrieves a boolean property value.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(val.trim());
    }

    /**
     * Retrieves a long property value.
     */
    public static long getLong(String key, long defaultValue) {
        String val = get(key);
        if (val == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid long value '{}' for key '{}', using default: {}", val, key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Retrieves a duration in seconds based on a configured integer key.
     */
    public static Duration getDurationInSeconds(String key, long defaultSeconds) {
        long seconds = getLong(key, defaultSeconds);
        return Duration.ofSeconds(seconds);
    }

    /**
     * Dynamically sets or overrides a property at runtime.
     */
    public static void setProperty(String key, String value) {
        if (key != null && value != null) {
            APP_PROPS.setProperty(key, value);
        }
    }
}
