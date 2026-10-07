package driver;

import config.ConfigConstants;
import config.ConfigReader;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.io.File;
import java.time.Duration;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating and customizing UiAutomator2Options for Android real devices and emulators.
 */
public final class UiAutomator2OptionsFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiAutomator2OptionsFactory.class);

    private UiAutomator2OptionsFactory() {
        // Prevent instantiation
    }

    /**
     * Builds standard UiAutomator2Options using configuration values.
     *
     * @return populated UiAutomator2Options instance
     */
    public static UiAutomator2Options buildDefaultOptions() {
        UiAutomator2Options options = new UiAutomator2Options();

        // Platform & Automation Engine
        options.setPlatformName(ConfigReader.get(ConfigConstants.PLATFORM_NAME, "Android"));
        options.setAutomationName(ConfigReader.get(ConfigConstants.AUTOMATION_NAME, "UiAutomator2"));

        // Device Details
        String deviceName = ConfigReader.get(ConfigConstants.DEVICE_NAME, "Android Device");
        options.setDeviceName(deviceName);

        String udid = ConfigReader.get(ConfigConstants.UDID, null);
        if (udid != null && !udid.trim().isEmpty()) {
            options.setUdid(udid.trim());
            LOGGER.info("Configured target device UDID: {}", udid);
        }

        String platformVersion = ConfigReader.get(ConfigConstants.PLATFORM_VERSION, null);
        if (platformVersion != null && !platformVersion.trim().isEmpty()) {
            options.setPlatformVersion(platformVersion.trim());
        }

        // Application Path or Package/Activity
        String appPath = ConfigReader.get(ConfigConstants.APP_PATH, null);
        if (appPath != null && !appPath.trim().isEmpty()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
                LOGGER.info("Configured application APK: {}", appFile.getAbsolutePath());
            } else {
                LOGGER.warn("Specified app.path does not exist on filesystem: {}", appPath);
            }
        }

        String appPackage = ConfigReader.get(ConfigConstants.APP_PACKAGE, null);
        String appActivity = ConfigReader.get(ConfigConstants.APP_ACTIVITY, null);
        if (appPackage != null && !appPackage.trim().isEmpty()) {
            options.setAppPackage(appPackage.trim());
            LOGGER.info("Configured app package: {}", appPackage);
        }
        if (appActivity != null && !appActivity.trim().isEmpty()) {
            options.setAppActivity(appActivity.trim());
            LOGGER.info("Configured app activity: {}", appActivity);
        }

        // Real Device Optimizations & Lifecycle
        options.setNoReset(ConfigReader.getBoolean(ConfigConstants.NO_RESET, true));
        options.setFullReset(ConfigReader.getBoolean(ConfigConstants.FULL_RESET, false));
        options.setAutoGrantPermissions(ConfigReader.getBoolean(ConfigConstants.AUTO_GRANT_PERMISSIONS, true));

        // Timeouts
        long newCommandTimeout = ConfigReader.getLong(ConfigConstants.NEW_COMMAND_TIMEOUT_SECONDS, 300);
        options.setNewCommandTimeout(Duration.ofSeconds(newCommandTimeout));

        long adbExecTimeout = ConfigReader.getLong(ConfigConstants.ADB_EXEC_TIMEOUT_MS, 60000);
        options.setAdbExecTimeout(Duration.ofMillis(adbExecTimeout));

        long serverInstallTimeout = ConfigReader.getLong(ConfigConstants.UIAUTOMATOR2_SERVER_INSTALL_TIMEOUT_MS, 120000);
        options.setUiautomator2ServerInstallTimeout(Duration.ofMillis(serverInstallTimeout));

        // Android modern OS compatibility
        if (ConfigReader.getBoolean(ConfigConstants.IGNORE_HIDDEN_API_POLICY_ERROR, true)) {
            options.setCapability("appium:ignoreHiddenApiPolicyError", true);
        }

        LOGGER.info("Constructed UiAutomator2Options: {}", options);
        return options;
    }

    /**
     * Builds UiAutomator2Options tailored for an installed application using package and activity.
     */
    public static UiAutomator2Options buildAppPackageOptions(String deviceName, String udid, String appPackage, String appActivity) {
        UiAutomator2Options options = buildDefaultOptions();
        if (deviceName != null) {
            options.setDeviceName(deviceName);
        }
        if (udid != null) {
            options.setUdid(udid);
        }
        if (appPackage != null) {
            options.setAppPackage(appPackage);
        }
        if (appActivity != null) {
            options.setAppActivity(appActivity);
        }
        return options;
    }

    /**
     * Builds UiAutomator2Options with custom capability overrides.
     */
    public static UiAutomator2Options buildCustomOptions(Map<String, Object> additionalCapabilities) {
        UiAutomator2Options options = buildDefaultOptions();
        if (additionalCapabilities != null) {
            additionalCapabilities.forEach(options::setCapability);
        }
        return options;
    }
}
