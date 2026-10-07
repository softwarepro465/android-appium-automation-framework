package driver;

import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe manager for AndroidDriver instances across test threads.
 */
public final class DriverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {
        // Prevent instantiation
    }

    /**
     * Gets the AndroidDriver instance for the calling thread.
     *
     * @return active AndroidDriver
     * @throws IllegalStateException if the driver has not been initialized for this thread
     */
    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            LOGGER.error("Driver accessed before initialization on thread: {}", Thread.currentThread().getName());
            throw new IllegalStateException("AndroidDriver is not initialized for thread ["
                    + Thread.currentThread().getName() + "]. Call DriverFactory.initDriver() before accessing driver.");
        }
        return driver;
    }

    /**
     * Sets the AndroidDriver instance for the calling thread.
     *
     * @param driver AndroidDriver instance to set
     */
    public static void setDriver(AndroidDriver driver) {
        if (driver != null) {
            DRIVER_THREAD_LOCAL.set(driver);
            LOGGER.info("AndroidDriver initialized and assigned to thread: {}", Thread.currentThread().getName());
        }
    }

    /**
     * Checks if a valid driver session is currently active for the calling thread.
     *
     * @return true if driver exists and session id is non-null
     */
    public static boolean isDriverActive() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        return driver != null && driver.getSessionId() != null;
    }

    /**
     * Safely quits the AndroidDriver session and clears the thread-local storage.
     */
    public static void quitDriver() {
        AndroidDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                LOGGER.info("Terminating AndroidDriver session for thread: {}", Thread.currentThread().getName());
                driver.quit();
            } catch (Exception e) {
                LOGGER.warn("Exception occurred while quitting AndroidDriver: {}", e.getMessage());
            } finally {
                DRIVER_THREAD_LOCAL.remove();
                LOGGER.info("Driver reference cleaned up for thread: {}", Thread.currentThread().getName());
            }
        }
    }
}
