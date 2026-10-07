package listeners;

import config.ConfigConstants;
import config.ConfigReader;
import driver.DriverManager;
import java.io.File;
import java.util.Map;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reporting.ExtentManager;
import reporting.ExtentTestManager;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

/**
 * Main TestNG Listener handling suite lifecycle, test execution tracking,
 * failure screenshot captures, and ExtentReports generation.
 */
public class TestListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        LoggerUtil.section("SUITE EXECUTION START: " + suite.getName());

        // Check if an application configuration file exists and load it
        String appConfig = "apps" + File.separator + "CardManager" + File.separator + "config.properties";
        ConfigReader.loadAppProperties(appConfig);

        // Allow testng.xml suite parameters to override configuration
        Map<String, String> xmlParams = suite.getXmlSuite().getParameters();
        if (xmlParams != null) {
            xmlParams.forEach(ConfigReader::setProperty);
        }

        // Initialize ExtentReports
        ExtentManager.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        LoggerUtil.section("SUITE EXECUTION FINISH: " + suite.getName());
        ExtentManager.flushReports();
    }

    @Override
    public void onStart(ITestContext context) {
        LoggerUtil.info("Starting Test Section: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LoggerUtil.info("Finished Test Section: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = getTestMethodName(result);
        String description = result.getMethod().getDescription();

        LoggerUtil.startTestCase(testName);
        ExtentTestManager.startTest(testName, description);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getTestMethodName(result);
        LoggerUtil.endTestCase(testName, "PASSED");
        ExtentTestManager.logPass("Test passed successfully: " + testName);

        if (ConfigReader.getBoolean(ConfigConstants.SCREENSHOT_ON_PASS, false)) {
            captureAndAttachScreenshot(testName + "_PASS");
        }

        ExtentTestManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestMethodName(result);
        Throwable throwable = result.getThrowable();

        LoggerUtil.error("Test FAILED: {}", testName);
        if (throwable != null) {
            LoggerUtil.error("Failure Reason: {}", throwable.getMessage());
        }

        // Handle failure screenshot
        if (ConfigReader.getBoolean(ConfigConstants.SCREENSHOT_ON_FAILURE, true)) {
            captureAndAttachScreenshot(testName + "_FAILURE");
        }

        ExtentTestManager.logFail("Test failed with exception: " + (throwable != null ? throwable.getMessage() : "Unknown"), throwable);
        LoggerUtil.endTestCase(testName, "FAILED");

        ExtentTestManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestMethodName(result);
        Throwable throwable = result.getThrowable();

        LoggerUtil.warn("Test SKIPPED: {}", testName);
        ExtentTestManager.logSkip("Test skipped: " + (throwable != null ? throwable.getMessage() : "No details"));
        LoggerUtil.endTestCase(testName, "SKIPPED");

        ExtentTestManager.endTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Handled as standard failure
        onTestFailure(result);
    }

    /**
     * Captures a screenshot (file and Base64) and attaches it to the active report.
     */
    private void captureAndAttachScreenshot(String screenshotName) {
        if (DriverManager.isDriverActive()) {
            // Save file
            ScreenshotUtil.captureScreenshotFile(screenshotName);

            // Attach Base64 to ExtentReport
            if (ConfigReader.getBoolean(ConfigConstants.ATTACH_SCREENSHOT_TO_REPORT, true)) {
                String base64 = ScreenshotUtil.captureScreenshotAsBase64();
                if (base64 != null) {
                    ExtentTestManager.attachScreenshotBase64(base64, screenshotName);
                }
            }
        }
    }

    /**
     * Resolves human-readable method name including class name.
     */
    private String getTestMethodName(ITestResult result) {
        return result.getMethod().getMethodName();
    }
}
