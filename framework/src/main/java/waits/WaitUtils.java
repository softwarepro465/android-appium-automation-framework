package waits;

import config.ConfigConstants;
import config.ConfigReader;
import driver.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reusable explicit wait utility built on top of WebDriverWait and FluentWait.
 * Provides fine-grained synchronization for mobile elements.
 */
public class WaitUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitUtils.class);

    private final AndroidDriver driver;
    private final Duration defaultTimeout;
    private final Duration defaultPolling;

    public WaitUtils() {
        this(DriverManager.getDriver());
    }

    public WaitUtils(AndroidDriver driver) {
        this.driver = driver;
        long timeoutSec = ConfigReader.getLong(ConfigConstants.EXPLICIT_WAIT_SECONDS, 15);
        long pollingMs = ConfigReader.getLong(ConfigConstants.POLLING_EVERY_MS, 500);
        this.defaultTimeout = Duration.ofSeconds(timeoutSec);
        this.defaultPolling = Duration.ofMillis(pollingMs);
    }

    public WaitUtils(AndroidDriver driver, Duration defaultTimeout) {
        this.driver = driver;
        this.defaultTimeout = defaultTimeout;
        this.defaultPolling = Duration.ofMillis(500);
    }

    /**
     * Creates a WebDriverWait instance with configured timeouts and ignored exceptions.
     */
    public WebDriverWait getWait(Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout, defaultPolling);
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(ElementNotInteractableException.class);
        return wait;
    }

    public WebDriverWait getWait() {
        return getWait(defaultTimeout);
    }

    // ==========================================
    // Visibility Waits
    // ==========================================

    public WebElement waitForVisibility(WebElement element) {
        return waitForVisibility(element, defaultTimeout);
    }

    public WebElement waitForVisibility(WebElement element, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, defaultTimeout);
    }

    public WebElement waitForVisibility(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, defaultTimeout);
    }

    public List<WebElement> waitForAllVisible(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // ==========================================
    // Invisibility Waits
    // ==========================================

    public boolean waitForInvisibility(WebElement element) {
        return waitForInvisibility(element, defaultTimeout);
    }

    public boolean waitForInvisibility(WebElement element, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean waitForInvisibility(By locator) {
        return waitForInvisibility(locator, defaultTimeout);
    }

    public boolean waitForInvisibility(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ==========================================
    // Clickability Waits
    // ==========================================

    public WebElement waitForClickability(WebElement element) {
        return waitForClickability(element, defaultTimeout);
    }

    public WebElement waitForClickability(WebElement element, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForClickability(By locator) {
        return waitForClickability(locator, defaultTimeout);
    }

    public WebElement waitForClickability(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ==========================================
    // Presence Waits
    // ==========================================

    public WebElement waitForPresence(By locator) {
        return waitForPresence(locator, defaultTimeout);
    }

    public WebElement waitForPresence(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForAllPresent(By locator) {
        return waitForAllPresent(locator, defaultTimeout);
    }

    public List<WebElement> waitForAllPresent(By locator, Duration timeout) {
        return getWait(timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // ==========================================
    // Text and Attribute Waits
    // ==========================================

    public boolean waitForTextToBePresent(WebElement element, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean waitForTextToBePresent(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForAttributeToContain(WebElement element, String attribute, String value) {
        return getWait().until(ExpectedConditions.attributeContains(element, attribute, value));
    }

    // ==========================================
    // Non-throwing Boolean Inquiries
    // ==========================================

    /**
     * Checks if an element is visible within the given timeout without throwing TimeoutException.
     */
    public boolean isElementVisible(By locator, Duration timeout) {
        try {
            waitForVisibility(locator, timeout);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Checks if an element is visible within the default timeout.
     */
    public boolean isElementVisible(By locator) {
        return isElementVisible(locator, defaultTimeout);
    }

    /**
     * Checks if an element is present within the given timeout without throwing TimeoutException.
     */
    public boolean isElementPresent(By locator, Duration timeout) {
        try {
            waitForPresence(locator, timeout);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Waits for any generic ExpectedCondition.
     */
    public <T> T waitForCondition(ExpectedCondition<T> condition, Duration timeout) {
        return getWait(timeout).until(condition);
    }

    /**
     * Pauses the current thread for the specified duration.
     * To be used sparingly only when required for native animations or transitions.
     */
    public static void pause(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Thread interrupted during pause: {}", e.getMessage());
        }
    }
}
