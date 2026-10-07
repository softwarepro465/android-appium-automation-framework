package actions;

import driver.DriverManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LoggerUtil;
import waits.WaitUtils;

/**
 * Common reusable Android interactions and W3C standard mobile gestures.
 */
public class AndroidActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidActions.class);
    private final AndroidDriver driver;
    private final WaitUtils waitUtils;

    public AndroidActions() {
        this(DriverManager.getDriver(), new WaitUtils());
    }

    public AndroidActions(AndroidDriver driver) {
        this(driver, new WaitUtils(driver));
    }

    public AndroidActions(AndroidDriver driver, WaitUtils waitUtils) {
        this.driver = driver;
        this.waitUtils = waitUtils;
    }

    // ==========================================
    // Basic Element Actions
    // ==========================================

    public void click(WebElement element) {
        waitUtils.waitForClickability(element).click();
        LoggerUtil.debug("Clicked on element: {}", element);
    }

    public void click(By locator) {
        waitUtils.waitForClickability(locator).click();
        LoggerUtil.debug("Clicked on locator: {}", locator);
    }

    public void type(WebElement element, String text) {
        WebElement el = waitUtils.waitForVisibility(element);
        el.clear();
        el.sendKeys(text);
        LoggerUtil.debug("Typed text into element: {}", text);
    }

    public void type(By locator, String text) {
        WebElement el = waitUtils.waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
        LoggerUtil.debug("Typed text into locator {}: {}", locator, text);
    }

    public void clear(WebElement element) {
        waitUtils.waitForVisibility(element).clear();
    }

    public String getText(WebElement element) {
        return waitUtils.waitForVisibility(element).getText();
    }

    public String getText(By locator) {
        return waitUtils.waitForVisibility(locator).getText();
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDisplayed(By locator) {
        return waitUtils.isElementVisible(locator);
    }

    public String getAttribute(WebElement element, String attribute) {
        return waitUtils.waitForVisibility(element).getAttribute(attribute);
    }

    // ==========================================
    // Keyboard and Device Key Actions
    // ==========================================

    public void hideKeyboard() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
                LOGGER.info("Soft keyboard hidden successfully.");
            }
        } catch (Exception e) {
            LOGGER.debug("Keyboard was not open or could not be hidden: {}", e.getMessage());
        }
    }

    public void pressKey(AndroidKey key) {
        driver.pressKey(new KeyEvent(key));
        LoggerUtil.debug("Pressed Android key: {}", key);
    }

    public void pressBack() {
        pressKey(AndroidKey.BACK);
    }

    public void pressHome() {
        pressKey(AndroidKey.HOME);
    }

    public void pressEnter() {
        pressKey(AndroidKey.ENTER);
    }

    // ==========================================
    // W3C Standard Gestures
    // ==========================================

    /**
     * Taps at specific screen coordinates using modern W3C Actions.
     */
    public void tapByCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(100)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(tap));
        LoggerUtil.debug("Tapped coordinates: ({}, {})", x, y);
    }

    /**
     * Executes a swipe gesture from start to end coordinates.
     */
    public void swipe(int startX, int startY, int endX, int endY, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipe));
        LoggerUtil.debug("Swiped from ({}, {}) to ({}, {}) over {}ms", startX, startY, endX, endY, duration.toMillis());
    }

    /**
     * Swipes upwards on the screen (scrolls content down).
     */
    public void swipeUp(double percentage) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.80);
        int endY = (int) (startY - (size.height * percentage));
        swipe(startX, startY, startX, Math.max(endY, 0), Duration.ofMillis(600));
    }

    public void swipeUp() {
        swipeUp(0.50);
    }

    /**
     * Swipes downwards on the screen (scrolls content up).
     */
    public void swipeDown(double percentage) {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.20);
        int endY = (int) (startY + (size.height * percentage));
        swipe(startX, startY, startX, Math.min(endY, size.height), Duration.ofMillis(600));
    }

    public void swipeDown() {
        swipeDown(0.50);
    }

    /**
     * Swipes horizontally towards the left.
     */
    public void swipeLeft(double percentage) {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.85);
        int endX = (int) (startX - (size.width * percentage));
        int y = size.height / 2;
        swipe(startX, y, Math.max(endX, 0), y, Duration.ofMillis(600));
    }

    public void swipeLeft() {
        swipeLeft(0.70);
    }

    /**
     * Swipes horizontally towards the right.
     */
    public void swipeRight(double percentage) {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.15);
        int endX = (int) (startX + (size.width * percentage));
        int y = size.height / 2;
        swipe(startX, y, Math.min(endX, size.width), y, Duration.ofMillis(600));
    }

    public void swipeRight() {
        swipeRight(0.70);
    }

    /**
     * Performs a long press on an element.
     */
    public void longPress(WebElement element, Duration duration) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + (size.getWidth() / 2);
        int centerY = location.getY() + (size.getHeight() / 2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);
        longPress.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(new Pause(finger, duration));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPress));
        LoggerUtil.debug("Performed long press on element at ({}, {}) for {}ms", centerX, centerY, duration.toMillis());
    }

    /**
     * Drags from source element to target element.
     */
    public void dragAndDrop(WebElement source, WebElement target) {
        Point srcLoc = source.getLocation();
        Point targetLoc = target.getLocation();
        Dimension srcSize = source.getSize();
        Dimension targetSize = target.getSize();

        int startX = srcLoc.getX() + (srcSize.getWidth() / 2);
        int startY = srcLoc.getY() + (srcSize.getHeight() / 2);
        int endX = targetLoc.getX() + (targetSize.getWidth() / 2);
        int endY = targetLoc.getY() + (targetSize.getHeight() / 2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 1);
        drag.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        drag.addAction(new Pause(finger, Duration.ofMillis(500)));
        drag.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), endX, endY));
        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(drag));
        LoggerUtil.debug("Dragged element from ({}, {}) to ({}, {})", startX, startY, endX, endY);
    }

    // ==========================================
    // Android UiAutomator Native Scrolling
    // ==========================================

    /**
     * Scrolls natively to an element containing visible text using UiScrollable.
     */
    public WebElement scrollIntoViewByText(String text) {
        String uiAutomatorString = String.format(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"%s\"))",
                text);
        LoggerUtil.info("Scrolling into view for text containing: '{}'", text);
        return driver.findElement(AppiumBy.androidUIAutomator(uiAutomatorString));
    }

    /**
     * Scrolls natively to an element matching exact content description using UiScrollable.
     */
    public WebElement scrollIntoViewByDescription(String contentDesc) {
        String uiAutomatorString = String.format(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionContains(\"%s\"))",
                contentDesc);
        LoggerUtil.info("Scrolling into view for content-desc: '{}'", contentDesc);
        return driver.findElement(AppiumBy.androidUIAutomator(uiAutomatorString));
    }

    // ==========================================
    // App Lifecycle Controls
    // ==========================================

    public void activateApp(String appPackage) {
        driver.activateApp(appPackage);
        LOGGER.info("Activated application: {}", appPackage);
    }

    public void terminateApp(String appPackage) {
        driver.terminateApp(appPackage);
        LOGGER.info("Terminated application: {}", appPackage);
    }

    public boolean isAppInstalled(String appPackage) {
        return driver.isAppInstalled(appPackage);
    }

    public void runAppInBackground(Duration duration) {
        driver.runAppInBackground(duration);
        LOGGER.info("Ran app in background for {} seconds", duration.getSeconds());
    }

    public WaitUtils getWaitUtils() {
        return waitUtils;
    }

    public AndroidDriver getDriver() {
        return driver;
    }
}
