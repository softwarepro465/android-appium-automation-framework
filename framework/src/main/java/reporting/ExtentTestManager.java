package reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe manager for individual test reporting nodes in ExtentReports.
 */
public final class ExtentTestManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtentTestManager.class);
    private static final ThreadLocal<ExtentTest> TEST_THREAD_LOCAL = new ThreadLocal<>();

    private ExtentTestManager() {
        // Prevent instantiation
    }

    /**
     * Retrieves the current thread's active ExtentTest instance.
     */
    public static ExtentTest getTest() {
        return TEST_THREAD_LOCAL.get();
    }

    /**
     * Creates and registers a new ExtentTest instance for the calling thread.
     */
    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = ExtentManager.getExtentReports().createTest(testName, description);
        TEST_THREAD_LOCAL.set(test);
        LOGGER.info("Started ExtentTest for: {}", testName);
        return test;
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    /**
     * Logs informational step in the active test.
     */
    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    /**
     * Logs passed verification in the active test.
     */
    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }

    /**
     * Logs failure with an optional throwable.
     */
    public static void logFail(String message, Throwable throwable) {
        ExtentTest test = getTest();
        if (test != null) {
            if (throwable != null) {
                test.log(Status.FAIL, message);
                test.log(Status.FAIL, throwable);
            } else {
                test.log(Status.FAIL, message);
            }
        }
    }

    /**
     * Logs skipped test details.
     */
    public static void logSkip(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.SKIP, message);
        }
    }

    /**
     * Attaches a Base64-encoded screenshot to the active test report.
     */
    public static void attachScreenshotBase64(String base64Image, String title) {
        ExtentTest test = getTest();
        if (test != null && base64Image != null && !base64Image.isEmpty()) {
            try {
                test.info(title, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image, title).build());
            } catch (Exception e) {
                LOGGER.error("Failed to attach Base64 screenshot to ExtentTest: {}", e.getMessage());
            }
        }
    }

    /**
     * Unloads the ExtentTest reference from the current thread.
     */
    public static void endTest() {
        TEST_THREAD_LOCAL.remove();
    }
}
