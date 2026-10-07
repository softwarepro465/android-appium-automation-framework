package driver;

import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.LoggerUtil;

/**
 * Reusable BaseTest providing standard driver setup and teardown lifecycle for TestNG tests.
 * Test classes across different applications can extend this base class.
 */
public abstract class BaseTest {

    /**
     * Initializes the AndroidDriver session before each test execution.
     * Optional TestNG parameters allow overriding device/session details per suite/test block.
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"deviceName", "udid", "platformVersion"})
    public void setUp(
            @Optional String deviceName,
            @Optional String udid,
            @Optional String platformVersion) {

        LoggerUtil.info("Setting up AndroidDriver session...");
        if (deviceName != null || udid != null || platformVersion != null) {
            var options = UiAutomator2OptionsFactory.buildDefaultOptions();
            if (deviceName != null) options.setDeviceName(deviceName);
            if (udid != null) options.setUdid(udid);
            if (platformVersion != null) options.setPlatformVersion(platformVersion);
            DriverFactory.initDriver(options);
        } else {
            DriverFactory.initDriver();
        }
    }

    /**
     * Quits the AndroidDriver session after each test execution to ensure session cleanliness.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LoggerUtil.info("Tearing down AndroidDriver session...");
        DriverFactory.quitDriver();
    }

    /**
     * Retrieves the current thread's active AndroidDriver instance.
     */
    protected AndroidDriver getDriver() {
        return DriverManager.getDriver();
    }
}
