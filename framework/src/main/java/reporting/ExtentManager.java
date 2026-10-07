package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import config.ConfigConstants;
import config.ConfigReader;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DateTimeUtil;
import utils.FileUtil;

/**
 * Singleton manager for ExtentReports test suite reporting.
 */
public final class ExtentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtentManager.class);
    private static ExtentReports extentReports;

    private ExtentManager() {
        // Prevent instantiation
    }

    /**
     * Initializes and configures the ExtentReports instance if not already created.
     *
     * @return active ExtentReports instance
     */
    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            initReports();
        }
        return extentReports;
    }

    /**
     * Configures the HTML Spark reporter and system metadata.
     */
    public static synchronized void initReports() {
        if (extentReports != null) {
            return;
        }

        String reportsDir = ConfigReader.get(ConfigConstants.REPORTS_DIR, "reports");
        FileUtil.ensureDirectoryExists(reportsDir);

        String reportFilePath = reportsDir + File.separator + "TestExecutionReport.html";
        LOGGER.info("Configuring ExtentReports HTML report at: {}", reportFilePath);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
        sparkReporter.config().setDocumentTitle("Android Automation Test Report");
        sparkReporter.config().setReportName("Mobile Test Automation Execution Results");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // System & Execution Information
        extentReports.setSystemInfo("Platform", ConfigReader.get(ConfigConstants.PLATFORM_NAME, "Android"));
        extentReports.setSystemInfo("Automation Name", ConfigReader.get(ConfigConstants.AUTOMATION_NAME, "UiAutomator2"));
        extentReports.setSystemInfo("Device Name", ConfigReader.get(ConfigConstants.DEVICE_NAME, "Android Device"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Execution Timestamp", DateTimeUtil.getDisplayDateTime());

        LOGGER.info("ExtentReports initialized successfully.");
    }

    /**
     * Flushes report data to disk.
     */
    public static synchronized void flushReports() {
        if (extentReports != null) {
            try {
                extentReports.flush();
                LOGGER.info("ExtentReports successfully flushed to disk.");
            } catch (Exception e) {
                LOGGER.error("Failed to flush ExtentReports: {}", e.getMessage(), e);
            }
        }
    }
}
