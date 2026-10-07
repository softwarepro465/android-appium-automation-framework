package utils;

import config.ConfigConstants;
import config.ConfigReader;
import driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reusable screenshot utility for capturing device screen as files and Base64 strings.
 */
public final class ScreenshotUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot from the current thread's driver and saves it as a PNG file.
     *
     * @param screenshotName Desired prefix for the screenshot file
     * @return File object pointing to the saved screenshot, or null if capture failed
     */
    public static File captureScreenshotFile(String screenshotName) {
        if (!DriverManager.isDriverActive()) {
            LOGGER.warn("Cannot capture screenshot file: AndroidDriver is not active on this thread.");
            return null;
        }
        return captureScreenshotFile(DriverManager.getDriver(), screenshotName);
    }

    /**
     * Captures a screenshot from the specified driver instance and saves it as a PNG file.
     *
     * @param driver         AndroidDriver instance
     * @param screenshotName Desired prefix for the screenshot file
     * @return File object pointing to the saved screenshot, or null if capture failed
     */
    public static File captureScreenshotFile(AndroidDriver driver, String screenshotName) {
        if (driver == null) {
            LOGGER.warn("Driver is null; skipping screenshot capture.");
            return null;
        }

        try {
            String targetDir = ConfigReader.get(ConfigConstants.SCREENSHOTS_DIR, "screenshots");
            FileUtil.ensureDirectoryExists(targetDir);

            String sanitizedName = sanitizeFilename(screenshotName);
            String fileName = String.format("%s_%s.png", sanitizedName, DateTimeUtil.getCurrentTimestamp());
            File destinationFile = new File(targetDir, fileName);

            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, destinationFile);

            LOGGER.info("Screenshot captured successfully: {}", destinationFile.getAbsolutePath());
            return destinationFile;
        } catch (IOException e) {
            LOGGER.error("Failed to save screenshot file: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            LOGGER.error("Unexpected error capturing screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Captures a screenshot as a Base64-encoded string using the current thread's driver.
     * Ideal for embedding directly into ExtentReports or Allure without file dependency.
     *
     * @return Base64-encoded image string, or null if capture failed
     */
    public static String captureScreenshotAsBase64() {
        if (!DriverManager.isDriverActive()) {
            LOGGER.warn("Cannot capture Base64 screenshot: AndroidDriver is not active.");
            return null;
        }
        return captureScreenshotAsBase64(DriverManager.getDriver());
    }

    /**
     * Captures a screenshot as a Base64-encoded string using the specified driver.
     *
     * @param driver AndroidDriver instance
     * @return Base64-encoded image string, or null if capture failed
     */
    public static String captureScreenshotAsBase64(AndroidDriver driver) {
        if (driver == null) {
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            LOGGER.error("Failed to capture Base64 screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Sanitizes filename string to avoid invalid filesystem characters.
     */
    private static String sanitizeFilename(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "screenshot";
        }
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
