package driver;

import config.ConfigConstants;
import config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class responsible for initializing and configuring AndroidDriver instances.
 */
public final class DriverFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
        // Prevent instantiation
    }

    /**
     * Initializes an AndroidDriver with default UiAutomator2 options and stores it in DriverManager.
     *
     * @return initialized AndroidDriver
     */
    public static AndroidDriver initDriver() {
        return initDriver(UiAutomator2OptionsFactory.buildDefaultOptions());
    }

    /**
     * Initializes an AndroidDriver with custom UiAutomator2 options and stores it in DriverManager.
     *
     * @param options configured UiAutomator2Options
     * @return initialized AndroidDriver
     */
    public static AndroidDriver initDriver(UiAutomator2Options options) {
        AndroidDriver driver = createDriver(options);
        DriverManager.setDriver(driver);
        return driver;
    }

    /**
     * Creates an AndroidDriver instance without registering it into ThreadLocal.
     *
     * @param options configured UiAutomator2Options
     * @return new AndroidDriver instance
     */
    public static AndroidDriver createDriver(UiAutomator2Options options) {
        String serverUrlStr = ConfigReader.get(ConfigConstants.APPIUM_SERVER_URL, "http://127.0.0.1:4723/");
        URL serverUrl = parseServerUrl(serverUrlStr);

        LOGGER.info("Initiating AndroidDriver session to Appium server at: {}", serverUrl);
        AndroidDriver driver;
        try {
            driver = new AndroidDriver(serverUrl, options);
            LOGGER.info("AndroidDriver session successfully established: SessionId={}", driver.getSessionId());
        } catch (Exception e) {
            LOGGER.error("Failed to establish AndroidDriver session: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create AndroidDriver session: " + e.getMessage(), e);
        }

        // Apply implicit wait if configured (default is 0 for explicit wait supremacy)
        int implicitWait = ConfigReader.getInt(ConfigConstants.IMPLICIT_WAIT_SECONDS, 0);
        if (implicitWait > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            LOGGER.info("Applied implicit wait of {} seconds", implicitWait);
        }

        return driver;
    }

    /**
     * Safely parses and normalizes the Appium server URL.
     */
    private static URL parseServerUrl(String urlStr) {
        try {
            return new URI(urlStr.trim()).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            LOGGER.error("Invalid Appium server URL format: '{}'", urlStr, e);
            throw new IllegalArgumentException("Malformed Appium server URL: " + urlStr, e);
        }
    }

    /**
     * Quits driver for the current thread.
     */
    public static void quitDriver() {
        DriverManager.quitDriver();
    }
}
