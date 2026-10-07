package reporting;

import utils.LoggerUtil;

/**
 * Unified logger bridging console/file logging and ExtentReports HTML dashboard.
 */
public final class TestReportLogger {

    private TestReportLogger() {
        // Prevent instantiation
    }

    /**
     * Logs an informational message to both logger and ExtentReport.
     */
    public static void logInfo(String message) {
        LoggerUtil.info(message);
        ExtentTestManager.logInfo(message);
    }

    /**
     * Logs a test step to both logger and ExtentReport.
     */
    public static void logStep(String stepDescription) {
        LoggerUtil.step(stepDescription);
        ExtentTestManager.logInfo("<b>STEP:</b> " + stepDescription);
    }

    /**
     * Logs a passed verification to both logger and ExtentReport.
     */
    public static void logPass(String message) {
        LoggerUtil.info("[PASS] {}", message);
        ExtentTestManager.logPass(message);
    }

    /**
     * Logs a failure message to both logger and ExtentReport.
     */
    public static void logFail(String message, Throwable throwable) {
        LoggerUtil.error("[FAIL] {}: {}", message, (throwable != null ? throwable.getMessage() : ""));
        ExtentTestManager.logFail(message, throwable);
    }
}
