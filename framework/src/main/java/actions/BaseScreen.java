package actions;

import config.ConfigConstants;
import config.ConfigReader;
import driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.LoggerUtil;
import waits.WaitUtils;

/**
 * Abstract BaseScreen representing the foundation for the Screen Object Model (SOM).
 * All application screen classes must extend this BaseScreen.
 *
 * Note: Following mobile automation standards, we use "Screen" terminology rather than "Page".
 */
public abstract class BaseScreen {

    protected final AndroidDriver driver;
    protected final WaitUtils waitUtils;
    protected final AndroidActions actions;

    /**
     * Initializes screen elements using AppiumFieldDecorator with configured timeout.
     *
     * @param driver active AndroidDriver instance
     */
    public BaseScreen(AndroidDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        this.actions = new AndroidActions(driver, waitUtils);

        long waitTimeout = ConfigReader.getLong(ConfigConstants.EXPLICIT_WAIT_SECONDS, 15);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(waitTimeout)), this);
    }

    /**
     * Convenience constructor fetching the driver from the thread-local DriverManager.
     */
    public BaseScreen() {
        this(DriverManager.getDriver());
    }

    // ==========================================
    // Screen Action Helpers
    // ==========================================

    protected void click(WebElement element) {
        actions.click(element);
    }

    protected void click(By locator) {
        actions.click(locator);
    }

    protected void type(WebElement element, String text) {
        actions.type(element, text);
    }

    protected void type(By locator, String text) {
        actions.type(locator, text);
    }

    protected void clear(WebElement element) {
        actions.clear(element);
    }

    protected String getText(WebElement element) {
        return actions.getText(element);
    }

    protected String getText(By locator) {
        return actions.getText(locator);
    }

    protected boolean isDisplayed(WebElement element) {
        return actions.isDisplayed(element);
    }

    protected boolean isDisplayed(By locator) {
        return actions.isDisplayed(locator);
    }

    protected WebElement waitForVisibility(WebElement element) {
        return waitUtils.waitForVisibility(element);
    }

    protected WebElement waitForVisibility(By locator) {
        return waitUtils.waitForVisibility(locator);
    }

    protected WebElement waitForClickability(WebElement element) {
        return waitUtils.waitForClickability(element);
    }

    protected void hideKeyboard() {
        actions.hideKeyboard();
    }

    protected void pressBack() {
        actions.pressBack();
    }

    protected WebElement scrollToText(String text) {
        return actions.scrollIntoViewByText(text);
    }

    protected WebElement scrollToDescription(String contentDesc) {
        return actions.scrollIntoViewByDescription(contentDesc);
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    public AndroidActions getActions() {
        return actions;
    }

    public WaitUtils getWaitUtils() {
        return waitUtils;
    }
}
