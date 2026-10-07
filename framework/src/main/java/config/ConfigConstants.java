package config;

/**
 * Central repository of configuration property keys used across the automation framework.
 */
public final class ConfigConstants {

    private ConfigConstants() {
        // Prevent instantiation
    }

    // Appium Server
    public static final String APPIUM_SERVER_URL = "appium.server.url";
    public static final String APPIUM_SERVER_TIMEOUT_SECONDS = "appium.server.timeout.seconds";

    // Device & Platform Capabilities
    public static final String PLATFORM_NAME = "platform.name";
    public static final String AUTOMATION_NAME = "automation.name";
    public static final String DEVICE_NAME = "device.name";
    public static final String UDID = "udid";
    public static final String PLATFORM_VERSION = "platform.version";

    // Application Capabilities
    public static final String APP_PACKAGE = "app.package";
    public static final String APP_ACTIVITY = "app.activity";
    public static final String APP_PATH = "app.path";

    // Session Lifecycle & Permissions
    public static final String NO_RESET = "no.reset";
    public static final String FULL_RESET = "full.reset";
    public static final String AUTO_GRANT_PERMISSIONS = "auto.grant.permissions";
    public static final String IGNORE_HIDDEN_API_POLICY_ERROR = "ignore.hidden.api.policy.error";
    public static final String NEW_COMMAND_TIMEOUT_SECONDS = "new.command.timeout.seconds";
    public static final String ADB_EXEC_TIMEOUT_MS = "adb.exec.timeout.ms";
    public static final String UIAUTOMATOR2_SERVER_INSTALL_TIMEOUT_MS = "uiautomator2.server.install.timeout.ms";

    // Timeouts and Waits
    public static final String EXPLICIT_WAIT_SECONDS = "explicit.wait.seconds";
    public static final String IMPLICIT_WAIT_SECONDS = "implicit.wait.seconds";
    public static final String POLLING_EVERY_MS = "polling.every.ms";
    public static final String PAGE_LOAD_TIMEOUT_SECONDS = "page.load.timeout.seconds";

    // Reporting & Screenshots
    public static final String REPORTS_DIR = "reports.dir";
    public static final String SCREENSHOTS_DIR = "screenshots.dir";
    public static final String SCREENSHOT_ON_FAILURE = "screenshot.on.failure";
    public static final String SCREENSHOT_ON_PASS = "screenshot.on.pass";
    public static final String ATTACH_SCREENSHOT_TO_REPORT = "attach.screenshot.to.report";

    // Retry Execution
    public static final String RETRY_FAILED_TESTS = "retry.failed.tests";
    public static final String MAX_RETRY_COUNT = "max.retry.count";
}
