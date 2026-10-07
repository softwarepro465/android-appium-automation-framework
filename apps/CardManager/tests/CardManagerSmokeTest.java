package tests;

import driver.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class CardManagerSmokeTest extends BaseTest {


private static final String APP_PACKAGE =
        "com.codelinedynamics.digital.card.mobile.wallet";

private WebDriverWait wait(int seconds) {
    return new WebDriverWait(
            getDriver(),
            Duration.ofSeconds(seconds)
    );
}

// =========================================================
// GENERIC HELPERS
// =========================================================

private boolean isTextPresent(String text, int seconds) {

    try {

        wait(seconds).until(driver ->
                !driver.findElements(
                        AppiumBy.xpath(
                                "//*[contains(@text,\""
                                        + text
                                        + "\")]"
                        )
                ).isEmpty()
        );

        return true;

    } catch (Exception e) {

        return false;
    }
}

private boolean isContentDescriptionPresent(
        String contentDescription,
        int seconds) {

    try {

        wait(seconds).until(driver ->
                !driver.findElements(
                        AppiumBy.xpath(
                                "//*[@content-desc=\""
                                        + contentDescription
                                        + "\"]"
                        )
                ).isEmpty()
        );

        return true;

    } catch (Exception e) {

        return false;
    }
}

private boolean isElementPresent(
        String xpath,
        int seconds) {

    try {

        wait(seconds).until(driver ->
                !driver.findElements(
                        AppiumBy.xpath(xpath)
                ).isEmpty()
        );

        return true;

    } catch (Exception e) {

        return false;
    }
}

private boolean clickText(
        String text,
        int seconds) {

    try {

        WebElement element =
                wait(seconds).until(
                        ExpectedConditions.elementToBeClickable(
                                AppiumBy.xpath(
                                        "//*[contains(@text,\""
                                                + text
                                                + "\") "
                                                + "and @clickable='true']"
                                )
                        )
                );

        element.click();

        System.out.println(
                "[PASS] Clicked: " + text
        );

        return true;

    } catch (Exception e) {

        try {

            WebElement textElement =
                    wait(seconds).until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.xpath(
                                            "//*[contains(@text,\""
                                                    + text
                                                    + "\")]"
                                    )
                            )
                    );

            textElement.click();

            System.out.println(
                    "[PASS] Clicked text: " + text
            );

            return true;

        } catch (Exception ignored) {

            System.out.println(
                    "[INFO] Unable to click: " + text
            );

            return false;
        }
    }
}

private boolean clickContentDescription(
        String contentDescription,
        int seconds) {

    try {

        WebElement element =
                wait(seconds).until(
                        ExpectedConditions.elementToBeClickable(
                                AppiumBy.xpath(
                                        "//*[@content-desc=\""
                                                + contentDescription
                                                + "\"]"
                                )
                        )
                );

        element.click();

        System.out.println(
                "[PASS] Clicked content-desc: "
                        + contentDescription
        );

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] Unable to click content-desc: "
                        + contentDescription
        );

        return false;
    }
}

private boolean clickXPath(
        String xpath,
        int seconds) {

    try {

        WebElement element =
                wait(seconds).until(
                        ExpectedConditions.elementToBeClickable(
                                AppiumBy.xpath(xpath)
                        )
                );

        element.click();

        System.out.println(
                "[PASS] Clicked XPath: " + xpath
        );

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] XPath click failed: "
                        + xpath
        );

        return false;
    }
}

private boolean verifyText(
        String text,
        int seconds) {

    boolean present =
            isTextPresent(text, seconds);

    if (present) {

        System.out.println(
                "[PASS] Verified: " + text
        );

    } else {

        System.out.println(
                "[INFO] Not detected: " + text
        );
    }

    return present;
}

private void pause(long milliseconds) {

    try {

        Thread.sleep(milliseconds);

    } catch (InterruptedException e) {

        Thread.currentThread().interrupt();
    }
}

// =========================================================
// STARTUP SCREEN
// =========================================================

private void handleStartupScreen() {

    System.out.println();
    System.out.println(
            "[STEP] Detecting Startup Screen"
    );

    System.out.println(
            "[INFO] Waiting for App Open Ad / "
                    + "Language / Startup / Onboarding..."
    );

    boolean startupDetected =
            wait(30).until(driver -> {

                if (!driver.findElements(
                        AppiumBy.xpath(
                                "//*[contains(@text,\"Continue to app\")]"
                        )
                ).isEmpty()) {

                    return true;
                }

                if (!driver.findElements(
                        AppiumBy.xpath(
                                "//*[contains(@text,\"Languages\")]"
                        )
                ).isEmpty()) {

                    return true;
                }

                if (!driver.findElements(
                        AppiumBy.xpath(
                                "//*[contains(@text,\"Get Started\")]"
                        )
                ).isEmpty()) {

                    return true;
                }

                String[] onboardingControls = {
                        "Next",
                        "Skip",
                        "Done"
                };

                for (String control :
                        onboardingControls) {

                    if (!driver.findElements(
                            AppiumBy.xpath(
                                    "//*[contains(@text,\""
                                            + control
                                            + "\")]"
                            )
                    ).isEmpty()) {

                        return true;
                    }
                }

                return false;
            });

    Assert.assertTrue(
            startupDetected,
            "Startup screen, App Open Ad, Language, "
                    + "Get Started, or Onboarding was not "
                    + "detected within 30 seconds."
    );

    System.out.println(
            "[PASS] Startup state detected."
    );

    // -----------------------------------------------------
    // APP OPEN AD
    // -----------------------------------------------------

    if (isTextPresent("Continue to app", 2)) {

        System.out.println(
                "[INFO] App Open Ad detected."
        );

        boolean clicked =
                clickText(
                        "Continue to app",
                        10
                );

        if (!clicked) {

            try {

                getDriver().executeScript(
                        "mobile: clickGesture",
                        Map.of(
                                "x", 497,
                                "y", 116
                        )
                );

                clicked = true;

                System.out.println(
                        "[PASS] App Open Ad Continue "
                                + "clicked using coordinates."
                );

            } catch (Exception e) {

                System.out.println(
                        "[INFO] App Open Ad coordinate "
                                + "click failed."
                );
            }
        }

        Assert.assertTrue(
                clicked,
                "Unable to click Continue to app."
        );

        System.out.println(
                "[PASS] App Open Ad completed."
        );

        pause(1000);
    }

    // -----------------------------------------------------
    // GET STARTED
    // -----------------------------------------------------

    if (isTextPresent("Get Started", 5)) {

        System.out.println(
                "[INFO] Get Started screen detected."
        );

        boolean clicked =
                clickText(
                        "Get Started",
                        10
                );

        Assert.assertTrue(
                clicked,
                "Unable to click Get Started."
        );

        System.out.println(
                "[PASS] Get Started clicked."
        );

        pause(500);
    }

    // -----------------------------------------------------
    // LANGUAGE SCREEN
    // -----------------------------------------------------

    if (isTextPresent("Languages", 5)) {

        selectLanguage();
    }

    // -----------------------------------------------------
    // FINAL STARTUP STATE CHECK
    // -----------------------------------------------------

    boolean startupFlowReady =
            waitForAnyText(
                    new String[]{
                            "Next",
                            "Skip",
                            "Done",
                            "Languages"
                    },
                    10
            );

    if (startupFlowReady) {

        System.out.println(
                "[PASS] Startup flow is ready "
                        + "for onboarding."
        );

    } else {

        System.out.println(
                "[INFO] Onboarding controls not yet "
                        + "visible. completeOnboarding() "
                        + "will wait for them."
        );
    }
}

// =========================================================
// WAIT FOR ANY STARTUP TEXT
// =========================================================

private boolean waitForAnyText(
        String[] texts,
        int seconds) {

    try {

        return wait(seconds).until(driver -> {

            for (String text : texts) {

                if (!driver.findElements(
                        AppiumBy.xpath(
                                "//*[contains(@text,\""
                                        + text
                                        + "\")]"
                        )
                ).isEmpty()) {

                    return true;
                }
            }

            return false;
        });

    } catch (Exception e) {

        return false;
    }
}

// =========================================================
// LANGUAGE
// =========================================================

private void selectLanguage() {

    System.out.println();
    System.out.println(
            "[STEP] Selecting Language"
    );

    boolean languageScreen =
            waitForAnyText(
                    new String[]{
                            "Languages",
                            "English"
                    },
                    10
            );

    Assert.assertTrue(
            languageScreen,
            "Language screen was not detected."
    );

    System.out.println(
            "[PASS] Language screen detected."
    );

    String englishParentXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[32,406][688,546]']";

    boolean englishClicked =
            clickXPath(
                    englishParentXPath,
                    10
            );

    if (!englishClicked) {

        englishClicked =
                clickText(
                        "English",
                        5
                );
    }

    Assert.assertTrue(
            englishClicked,
            "Unable to select English."
    );

    pause(300);

    String languageCheckXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[612,88][708,184]']";

    boolean checkClicked =
            clickXPath(
                    languageCheckXPath,
                    10
            );

    if (!checkClicked) {

        try {

            getDriver().executeScript(
                    "mobile: clickGesture",
                    Map.of(
                            "x", 660,
                            "y", 136
                    )
            );

            checkClicked = true;

            System.out.println(
                    "[PASS] Language check clicked "
                            + "using coordinates."
            );

        } catch (Exception e) {

            System.out.println(
                    "[INFO] Language check "
                            + "coordinate click failed."
            );
        }
    }

    Assert.assertTrue(
            checkClicked,
            "Unable to confirm language selection."
    );

    System.out.println(
            "[PASS] Language selection completed."
    );

    pause(700);
}

// =========================================================
// ONBOARDING
// =========================================================

private void completeOnboarding() {

    System.out.println();
    System.out.println(
            "[STEP] Completing Onboarding"
    );

    // -----------------------------------------------------
    // SCREEN 1
    // -----------------------------------------------------

    System.out.println(
            "[STEP] Onboarding Screen 1"
    );

    waitForOnboardingTransition();

    verifyOnboardingControls();

    boolean screen1Clicked =
            clickNextOrSkip();

    Assert.assertTrue(
            screen1Clicked,
            "Unable to navigate from Onboarding Screen 1."
    );

    System.out.println(
            "[PASS] Onboarding navigation clicked."
    );

    pause(700);

    // -----------------------------------------------------
    // SCREEN 2
    // -----------------------------------------------------

    System.out.println(
            "[STEP] Onboarding Screen 2"
    );

    waitForOnboardingTransition();

    verifyOnboardingControls();

    boolean screen2Clicked =
            clickNextOrSkip();

    Assert.assertTrue(
            screen2Clicked,
            "Unable to navigate from Onboarding Screen 2."
    );

    System.out.println(
            "[PASS] Onboarding navigation clicked."
    );

    pause(700);

    // -----------------------------------------------------
    // SCREEN 3
    // -----------------------------------------------------

    System.out.println(
            "[STEP] Onboarding Screen 3"
    );

    waitForDoneButton();

    clickDone();

    // -----------------------------------------------------
    // INTERSTITIAL AD
    // -----------------------------------------------------

    closeInterstitialAd();

    // -----------------------------------------------------
    // PREMIUM
    // -----------------------------------------------------

    waitForPremiumScreen();

    System.out.println(
            "[PASS] All onboarding screens completed."
    );
}

private void waitForOnboardingTransition() {

    boolean onboarding =
            wait(20).until(driver -> {

                String[] controls = {
                        "Next",
                        "Skip",
                        "Done"
                };

                for (String control : controls) {

                    if (!driver.findElements(
                            AppiumBy.xpath(
                                    "//*[contains(@text,\""
                                            + control
                                            + "\")]"
                            )
                    ).isEmpty()) {

                        return true;
                    }
                }

                return false;
            });

    Assert.assertTrue(
            onboarding,
            "Onboarding screen did not appear."
    );

    System.out.println(
            "[PASS] Onboarding screen detected."
    );
}

private void verifyOnboardingControls() {

    boolean control =
            isTextPresent("Next", 3)
                    || isTextPresent("Skip", 3)
                    || isTextPresent("Done", 3);

    Assert.assertTrue(
            control,
            "Onboarding navigation control not found."
    );

    System.out.println(
            "[PASS] Onboarding control detected."
    );
}

private boolean clickNextOrSkip() {

    if (isTextPresent("Next", 2)) {

        return clickText(
                "Next",
                10
        );
    }

    if (isTextPresent("Skip", 2)) {

        return clickText(
                "Skip",
                10
        );
    }

    return false;
}

// =========================================================
// DONE BUTTON
// =========================================================

private void waitForDoneButton() {

    System.out.println(
            "[INFO] Locating Done button..."
    );

    String doneXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[469,1019][688,1115]']";

    boolean done =
            isElementPresent(
                    doneXPath,
                    15
            );

    Assert.assertTrue(
            done,
            "Done button was not displayed."
    );
}

private void clickDone() {

    String doneXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[469,1019][688,1115]']";

    boolean clicked =
            clickXPath(
                    doneXPath,
                    10
            );

    Assert.assertTrue(
            clicked,
            "Unable to click Done button."
    );

    System.out.println(
            "[PASS] Onboarding Done clicked."
    );

    pause(700);
}

// =========================================================
// INTERSTITIAL AD
// =========================================================

private void closeInterstitialAd() {

    System.out.println();
    System.out.println(
            "[STEP] Handling Interstitial Ad"
    );

    String closeButtonXPath =
            "//*[@content-desc='Close' "
                    + "and @clickable='true']";

    System.out.println(
            "[INFO] Waiting for Interstitial Ad "
                    + "Close button..."
    );

    boolean closeButtonDisplayed =
            isElementPresent(
                    closeButtonXPath,
                    15
            );

    Assert.assertTrue(
            closeButtonDisplayed,
            "Interstitial Ad Close button "
                    + "was not displayed."
    );

    System.out.println(
            "[PASS] Interstitial Ad detected."
    );

    boolean clicked =
            clickXPath(
                    closeButtonXPath,
                    10
            );

    if (!clicked) {

        clicked =
                clickContentDescription(
                        "Close",
                        5
                );
    }

    if (!clicked) {

        try {

            getDriver().executeScript(
                    "mobile: clickGesture",
                    Map.of(
                            "x", 672,
                            "y", 120
                    )
            );

            clicked = true;

            System.out.println(
                    "[PASS] Interstitial Ad Close "
                            + "clicked using coordinates."
            );

        } catch (Exception e) {

            System.out.println(
                    "[INFO] Interstitial Close "
                            + "coordinate click failed."
            );
        }
    }

    Assert.assertTrue(
            clicked,
            "Unable to close Interstitial Ad."
    );

    System.out.println(
            "[PASS] Interstitial Ad closed."
    );

    try {

        wait(10).until(driver ->
                driver.findElements(
                        AppiumBy.xpath(
                                closeButtonXPath
                        )
                ).isEmpty()
        );

        System.out.println(
                "[PASS] Interstitial Ad removed "
                        + "from the screen."
        );

    } catch (Exception e) {

        System.out.println(
                "[INFO] Interstitial Close button "
                        + "disappearance was not confirmed."
        );
    }

    pause(500);
}

// =========================================================
// PREMIUM SCREEN
// =========================================================

private boolean isPremiumContentPresent() {

    return isTextPresent("Go Premium", 1)
            || isTextPresent("Monthly", 1)
            || isTextPresent("Yearly", 1)
            || isTextPresent("LifeTime", 1)
            || isTextPresent(
                    "ACTIVATE YOUR WALLET",
                    1
            );
}

private void waitForPremiumScreen() {

    System.out.println(
            "[INFO] Waiting for Premium screen "
                    + "after Interstitial Ad..."
    );

    boolean premium =
            wait(20).until(driver -> {

                String[] premiumTexts = {
                        "Go Premium",
                        "Monthly",
                        "Yearly",
                        "LifeTime",
                        "ACTIVATE YOUR WALLET"
                };

                for (String text : premiumTexts) {

                    if (!driver.findElements(
                            AppiumBy.xpath(
                                    "//*[contains(@text,\""
                                            + text
                                            + "\")]"
                            )
                    ).isEmpty()) {

                        return true;
                    }
                }

                return false;
            });

    Assert.assertTrue(
            premium,
            "Premium screen did not appear after "
                    + "closing Interstitial Ad."
    );

    System.out.println(
            "[PASS] Premium screen detected."
    );
}

// =========================================================
// PREMIUM SCROLL HELPERS
// =========================================================

private boolean scrollPremiumScreenDown() {

    try {

        getDriver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "left", 34,
                        "top", 72,
                        "width", 652,
                        "height", 1075,
                        "direction", "down",
                        "percent", 0.75
                )
        );

        System.out.println(
                "[PASS] Premium screen scrolled down."
        );

        pause(700);

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] Premium screen scroll down failed: "
                        + e.getMessage()
        );

        return false;
    }
}

private boolean scrollPremiumScreenUp() {

    try {

        getDriver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "left", 34,
                        "top", 72,
                        "width", 652,
                        "height", 1075,
                        "direction", "up",
                        "percent", 0.75
                )
        );

        System.out.println(
                "[PASS] Premium screen scrolled up."
        );

        pause(700);

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] Premium screen scroll up failed: "
                        + e.getMessage()
        );

        return false;
    }
}

private boolean scrollUntilTextVisible(
        String text,
        int maxScrolls) {

    if (isTextPresent(text, 1)) {

        System.out.println(
                "[PASS] Premium text already visible: "
                        + text
        );

        return true;
    }

    for (int i = 0; i < maxScrolls; i++) {

        System.out.println(
                "[INFO] Scrolling Premium screen "
                        + "for "
                        + text
                        + " - attempt "
                        + (i + 1)
                        + "/"
                        + maxScrolls
        );

        boolean scrolled =
                scrollPremiumScreenDown();

        if (!scrolled) {

            break;
        }

        if (isTextPresent(text, 2)) {

            System.out.println(
                    "[PASS] Premium text visible "
                            + "after scroll: "
                            + text
            );

            return true;
        }
    }

    boolean visible =
            isTextPresent(text, 2);

    if (visible) {

        System.out.println(
                "[PASS] Premium text detected: "
                        + text
        );

    } else {

        System.out.println(
                "[INFO] Premium text still not "
                        + "detected: "
                        + text
        );
    }

    return visible;
}

private boolean clickTextClickableParent(
        String text,
        int seconds) {

    String clickableParentXPath =
            "//*[contains(@text,\""
                    + text
                    + "\")]/ancestor::*"
                    + "[@clickable='true'][1]";

    boolean clicked =
            clickXPath(
                    clickableParentXPath,
                    seconds
            );

    if (clicked) {

        System.out.println(
                "[PASS] Clicked clickable parent "
                        + "for: "
                        + text
        );

        return true;
    }

    return false;
}

// =========================================================
// PREMIUM PACKAGE CLICKS
// =========================================================

private void selectPremiumPackages() {

    System.out.println();
    System.out.println(
            "[STEP] Selecting Premium Packages"
    );

    waitForPremiumScreen();

    // -----------------------------------------------------
    // MONTHLY
    // -----------------------------------------------------

    String monthlyXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[34,809][686,971]']";

    System.out.println(
            "[INFO] Tapping Monthly package..."
    );

    boolean monthlyClicked =
            clickXPath(
                    monthlyXPath,
                    10
            );

    if (!monthlyClicked) {

        monthlyClicked =
                clickTextClickableParent(
                        "Monthly",
                        5
                );
    }

    Assert.assertTrue(
            monthlyClicked,
            "Unable to click Monthly package."
    );

    System.out.println(
            "[PASS] Monthly package selected."
    );

    pause(500);

    // -----------------------------------------------------
    // YEARLY
    // -----------------------------------------------------

    String yearlyXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[34,623][686,785]']";

    System.out.println(
            "[INFO] Tapping Yearly package..."
    );

    boolean yearlyClicked =
            clickXPath(
                    yearlyXPath,
                    10
            );

    if (!yearlyClicked) {

        yearlyClicked =
                clickTextClickableParent(
                        "Yearly",
                        5
                );
    }

    Assert.assertTrue(
            yearlyClicked,
            "Unable to click Yearly package."
    );

    System.out.println(
            "[PASS] Yearly package selected."
    );

    pause(500);

    // -----------------------------------------------------
    // LIFETIME
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Scrolling Premium screen "
                    + "to LifeTime package..."
    );

    boolean lifetimeVisible =
            scrollUntilTextVisible(
                    "LifeTime",
                    3
            );

    if (!lifetimeVisible) {

        lifetimeVisible =
                isElementPresent(
                        "//*[contains(@text,'LifeTime')]/ancestor::*[@clickable='true'][1]",
                        3
                );
    }

    if (!lifetimeVisible) {

        lifetimeVisible =
                isElementPresent(
                        "//*[@clickable='true' "
                                + "and @bounds='[34,995][686,1147]']",
                        3
                );
    }

    Assert.assertTrue(
            lifetimeVisible,
            "LifeTime package did not become visible "
                    + "after scrolling Premium screen."
    );

    System.out.println(
            "[PASS] LifeTime package is visible."
    );

    System.out.println(
            "[INFO] Tapping LifeTime package..."
    );

    boolean lifetimeClicked =
            clickTextClickableParent(
                    "LifeTime",
                    10
            );

    if (!lifetimeClicked) {

        lifetimeClicked =
                clickText(
                        "LifeTime",
                        5
                );
    }

    if (!lifetimeClicked) {

        lifetimeClicked =
                clickXPath(
                        "//*[@clickable='true' "
                                + "and @bounds='[34,995][686,1147]']",
                        5
                );
    }

    Assert.assertTrue(
            lifetimeClicked,
            "Unable to click LifeTime package "
                    + "after scrolling Premium screen."
    );

    System.out.println(
            "[PASS] LifeTime package selected."
    );

    pause(700);

    // -----------------------------------------------------
    // RETURN TO PREMIUM TOP
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Returning Premium screen "
                    + "to top..."
    );

    scrollPremiumScreenUp();

    System.out.println(
            "[PASS] Premium package selection completed."
    );
}

// =========================================================
// VERIFY PREMIUM SCREEN
// =========================================================

private void verifyPremiumScreen() {

    System.out.println();
    System.out.println(
            "[STEP] Verifying Premium Screen"
    );

    waitForPremiumScreen();

    Assert.assertTrue(
            verifyText("Monthly", 5),
            "Monthly package not found on Premium screen."
    );

    Assert.assertTrue(
            verifyText("Yearly", 5),
            "Yearly package not found on Premium screen."
    );

    System.out.println(
            "[PASS] Monthly and Yearly verified."
    );

    System.out.println(
            "[INFO] Scrolling Premium screen "
                    + "to verify LifeTime..."
    );

    boolean lifetimeVisible =
            scrollUntilTextVisible(
                    "LifeTime",
                    3
            );

    if (!lifetimeVisible) {

        lifetimeVisible =
                isElementPresent(
                        "//*[contains(@text,'LifeTime')]/ancestor::*[@clickable='true'][1]",
                        3
                );
    }

    if (!lifetimeVisible) {

        lifetimeVisible =
                isElementPresent(
                        "//*[@clickable='true' "
                                + "and @bounds='[34,995][686,1147]']",
                        3
                );
    }

    Assert.assertTrue(
            lifetimeVisible,
            "LifeTime package was not found on "
                    + "Premium screen."
    );

    System.out.println(
            "[PASS] LifeTime package verified."
    );

    boolean activateWallet =
            verifyText(
                    "ACTIVATE YOUR WALLET",
                    5
            );

    if (activateWallet) {

        System.out.println(
                "[PASS] ACTIVATE YOUR WALLET verified."
        );

    } else {

        System.out.println(
                "[INFO] ACTIVATE YOUR WALLET is not "
                        + "currently visible."
        );
    }

    System.out.println(
            "[INFO] Returning Premium screen "
                    + "to top after verification..."
    );

    scrollPremiumScreenUp();

    System.out.println(
            "[PASS] Premium screen verification completed."
    );
}

// =========================================================
// CLOSE PREMIUM SCREEN
// =========================================================

private void closePremiumScreen() {

    System.out.println();
    System.out.println(
            "[STEP] Closing Premium Screen"
    );

    System.out.println(
            "[INFO] Returning Premium screen to top..."
    );

    scrollPremiumScreenUp();

    pause(500);

    String premiumCrossXPath =
            "//*[@NAF='true' "
                    + "and @clickable='true' "
                    + "and @bounds='[22,88][118,184]']";

    System.out.println(
            "[INFO] Trying Premium Cross exact XPath..."
    );

    boolean clicked =
            clickXPath(
                    premiumCrossXPath,
                    5
            );

    if (!clicked) {

        String clickableTopLeftXPath =
                "//*[@clickable='true' "
                        + "and @bounds='[22,88][118,184]']";

        System.out.println(
                "[INFO] Trying clickable bounds fallback..."
        );

        clicked =
                clickXPath(
                        clickableTopLeftXPath,
                        5
                );
    }

    if (!clicked) {

        System.out.println(
                "[INFO] Trying Premium Cross "
                        + "coordinate fallback..."
        );

        try {

            getDriver().executeScript(
                    "mobile: clickGesture",
                    Map.of(
                            "x", 70,
                            "y", 136
                    )
            );

            clicked = true;

            System.out.println(
                    "[PASS] Premium Cross clicked "
                            + "using coordinates."
            );

        } catch (Exception e) {

            System.out.println(
                    "[INFO] Premium Cross coordinate "
                            + "click failed: "
                            + e.getMessage()
            );
        }
    }

    Assert.assertTrue(
            clicked,
            "Unable to close Premium screen."
    );

    System.out.println(
            "[PASS] Premium screen closed."
    );

    pause(700);

    System.out.println(
            "[INFO] Waiting for 30% OFF screen..."
    );

    boolean discountScreen =
            wait(15).until(driver -> {

                String[] discountTexts = {
                        "30% OFF",
                        "SKIP",
                        "Activate your wallet",
                        "ACTIVATE YOUR WALLET"
                };

                for (String text : discountTexts) {

                    if (!driver.findElements(
                            AppiumBy.xpath(
                                    "//*[contains(@text,\""
                                            + text
                                            + "\")]"
                            )
                    ).isEmpty()) {

                        return true;
                    }
                }

                return false;
            });

    Assert.assertTrue(
            discountScreen,
            "30% OFF screen did not appear "
                    + "after Premium screen."
    );

    System.out.println(
            "[PASS] 30% OFF screen detected."
    );
}

// =========================================================
// 30% OFF SCREEN SCROLL
// =========================================================

private boolean scrollDiscountScreenDown() {

    try {

        getDriver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "left", 20,
                        "top", 72,
                        "width", 680,
                        "height", 1140,
                        "direction", "down",
                        "percent", 0.70
                )
        );

        System.out.println(
                "[PASS] 30% OFF screen scrolled down."
        );

        pause(700);

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] 30% OFF screen scroll down failed: "
                        + e.getMessage()
        );

        return false;
    }
}

private boolean scrollDiscountScreenUp() {

    try {

        getDriver().executeScript(
                "mobile: scrollGesture",
                Map.of(
                        "left", 20,
                        "top", 72,
                        "width", 680,
                        "height", 1140,
                        "direction", "up",
                        "percent", 0.70
                )
        );

        System.out.println(
                "[PASS] 30% OFF screen scrolled up."
        );

        pause(700);

        return true;

    } catch (Exception e) {

        System.out.println(
                "[INFO] 30% OFF screen scroll up failed: "
                        + e.getMessage()
        );

        return false;
    }
}

private boolean scrollDiscountUntilTextVisible(
        String text,
        int maxScrolls) {

    if (isTextPresent(text, 2)) {

        System.out.println(
                "[PASS] 30% OFF text already visible: "
                        + text
        );

        return true;
    }

    for (int i = 0; i < maxScrolls; i++) {

        System.out.println(
                "[INFO] Scrolling 30% OFF screen "
                        + "for "
                        + text
                        + " - attempt "
                        + (i + 1)
                        + "/"
                        + maxScrolls
        );

        if (!scrollDiscountScreenDown()) {

            break;
        }

        if (isTextPresent(text, 2)) {

            System.out.println(
                    "[PASS] 30% OFF text visible "
                            + "after scroll: "
                            + text
            );

            return true;
        }
    }

    return isTextPresent(text, 2);
}

// =========================================================
// ACTIVATE YOUR WALLET
// =========================================================

private boolean clickActivateWallet() {

    System.out.println(
            "[INFO] Looking for ACTIVATE YOUR WALLET..."
    );

    boolean visible =
            scrollDiscountUntilTextVisible(
                    "ACTIVATE YOUR WALLET",
                    3
            );

    if (!visible) {

        visible =
                scrollDiscountUntilTextVisible(
                        "Activate your wallet",
                        3
                );
    }

    if (!visible) {

        System.out.println(
                "[INFO] Activate button not visible."
        );

        return false;
    }

    // Exact clickable parent from latest UI hierarchy:
    // bounds="[32,1371][688,1471]"

    String activateXPath =
            "//*[@clickable='true' "
                    + "and @bounds='[32,1371][688,1471]']";

    boolean clicked =
            clickXPath(
                    activateXPath,
                    10
            );

    if (!clicked) {

        clicked =
                clickTextClickableParent(
                        "ACTIVATE YOUR WALLET",
                        5
                );
    }

    if (!clicked) {

        clicked =
                clickTextClickableParent(
                        "Activate your wallet",
                        5
                );
    }

    if (!clicked) {

        clicked =
                clickText(
                        "ACTIVATE YOUR WALLET",
                        5
                );
    }

    if (!clicked) {

        clicked =
                clickText(
                        "Activate your wallet",
                        5
                );
    }

    if (!clicked) {

        try {

            getDriver().executeScript(
                    "mobile: clickGesture",
                    Map.of(
                            "x", 360,
                            "y", 1421
                    )
            );

            clicked = true;

            System.out.println(
                    "[PASS] Activate Your Wallet "
                            + "clicked using coordinates."
            );

        } catch (Exception e) {

            System.out.println(
                    "[INFO] Activate coordinate "
                            + "click failed: "
                            + e.getMessage()
            );
        }
    }

    return clicked;
}

// =========================================================
// CLOSE AFTER ACTIVATE
// =========================================================

private boolean closeAfterActivateWallet() {

    System.out.println();
    System.out.println(
            "[INFO] Checking for screen/popup "
                    + "after Activate Your Wallet..."
    );

    pause(1000);

    String closeXPath =
            "//*[@content-desc='Close' "
                    + "and @clickable='true']";

    boolean closeVisible =
            isElementPresent(
                    closeXPath,
                    5
            );

    if (closeVisible) {

        System.out.println(
                "[PASS] Close button detected."
        );

        boolean clicked =
                clickXPath(
                        closeXPath,
                        10
                );

        if (!clicked) {

            clicked =
                    clickContentDescription(
                            "Close",
                            5
                    );
        }

        if (!clicked) {

            try {

                getDriver().executeScript(
                        "mobile: clickGesture",
                        Map.of(
                                "x", 672,
                                "y", 120
                        )
                );

                clicked = true;

                System.out.println(
                        "[PASS] Close clicked using "
                                + "coordinate fallback."
                );

            } catch (Exception e) {

                System.out.println(
                        "[INFO] Close coordinate "
                                + "click failed: "
                                + e.getMessage()
                );
            }
        }

        return clicked;
    }

    // Generic Close text fallback
    if (isTextPresent("Close", 3)) {

        return clickText(
                "Close",
                5
        );
    }

    System.out.println(
            "[INFO] No Close button detected. "
                    + "Continuing to SKIP."
    );

    return true;
}

// =========================================================
// 30% OFF SCREEN
// =========================================================

private void handleDiscountScreen() {

    System.out.println();
    System.out.println(
            "[STEP] Handling 30% OFF Screen"
    );

    boolean discountScreen =
            wait(15).until(driver -> {

                String[] discountTexts = {
                        "30% OFF",
                        "SKIP",
                        "Activate your wallet",
                        "ACTIVATE YOUR WALLET"
                };

                for (String text : discountTexts) {

                    if (!driver.findElements(
                            AppiumBy.xpath(
                                    "//*[contains(@text,\""
                                            + text
                                            + "\")]"
                            )
                    ).isEmpty()) {

                        return true;
                    }
                }

                return false;
            });

    Assert.assertTrue(
            discountScreen,
            "30% OFF screen was not displayed."
    );

    System.out.println(
            "[PASS] 30% OFF screen detected."
    );

    // -----------------------------------------------------
    // MONTHLY
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Tapping Monthly on 30% OFF screen..."
    );

    boolean monthlyVisible =
            scrollDiscountUntilTextVisible(
                    "Monthly",
                    3
            );

    Assert.assertTrue(
            monthlyVisible,
            "Monthly option not found on 30% OFF screen."
    );

    boolean monthlyClicked =
            clickTextClickableParent(
                    "Monthly",
                    10
            );

    if (!monthlyClicked) {

        monthlyClicked =
                clickText(
                        "Monthly",
                        5
                );
    }

    Assert.assertTrue(
            monthlyClicked,
            "Unable to click Monthly on 30% OFF screen."
    );

    System.out.println(
            "[PASS] Monthly selected on 30% OFF screen."
    );

    pause(500);

    // -----------------------------------------------------
    // YEARLY
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Tapping Yearly on 30% OFF screen..."
    );

    boolean yearlyVisible =
            scrollDiscountUntilTextVisible(
                    "Yearly",
                    3
            );

    Assert.assertTrue(
            yearlyVisible,
            "Yearly option not found on 30% OFF screen."
    );

    boolean yearlyClicked =
            clickTextClickableParent(
                    "Yearly",
                    10
            );

    if (!yearlyClicked) {

        yearlyClicked =
                clickText(
                        "Yearly",
                        5
                );
    }

    Assert.assertTrue(
            yearlyClicked,
            "Unable to click Yearly on 30% OFF screen."
    );

    System.out.println(
            "[PASS] Yearly selected on 30% OFF screen."
    );

    pause(500);

    // -----------------------------------------------------
    // ACTIVATE YOUR WALLET
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Tapping Activate Your Wallet..."
    );

    boolean activateClicked =
            clickActivateWallet();

    Assert.assertTrue(
            activateClicked,
            "Unable to click Activate Your Wallet "
                    + "on 30% OFF screen."
    );

    System.out.println(
            "[PASS] Activate Your Wallet clicked."
    );

    pause(1000);

    // -----------------------------------------------------
    // CLOSE NEXT SCREEN / POPUP
    // -----------------------------------------------------

    boolean closeHandled =
            closeAfterActivateWallet();

    Assert.assertTrue(
            closeHandled,
            "Unable to close the screen/popup "
                    + "after Activate Your Wallet."
    );

    System.out.println(
            "[PASS] Post-activation screen/popup handled."
    );

    pause(700);

    // -----------------------------------------------------
    // SKIP
    // -----------------------------------------------------

    System.out.println(
            "[INFO] Scrolling to SKIP..."
    );

    boolean skipVisible =
            scrollDiscountUntilTextVisible(
                    "SKIP",
                    3
            );

    if (!skipVisible) {

        skipVisible =
                scrollDiscountUntilTextVisible(
                        "Skip",
                        3
                );
    }

    Assert.assertTrue(
            skipVisible,
            "SKIP option not found on 30% OFF screen."
    );

    System.out.println(
            "[PASS] SKIP option verified."
    );

    System.out.println(
            "[INFO] Clicking SKIP..."
    );

    // Exact confirmed UI hierarchy:
    // text="SKIP"
    // clickable="true"
    // bounds="[312,1121][408,1211]"

    String skipXPath =
            "//*[@text='SKIP' "
                    + "and @clickable='true']";

    boolean skipClicked =
            clickXPath(
                    skipXPath,
                    10
            );

    // -----------------------------------------------------
    // EXACT BOUNDS FALLBACK
    // -----------------------------------------------------

    if (!skipClicked) {

        System.out.println(
                "[INFO] Exact SKIP XPath failed. "
                        + "Trying bounds fallback..."
        );

        String skipBoundsXPath =
                "//*[@clickable='true' "
                        + "and @bounds='[312,1121][408,1211]']";

        skipClicked =
                clickXPath(
                        skipBoundsXPath,
                        5
                );
    }

    // -----------------------------------------------------
    // TEXT FALLBACK
    // -----------------------------------------------------

    if (!skipClicked) {

        skipClicked =
                clickText(
                        "SKIP",
                        5
                );
    }

    if (!skipClicked) {

        skipClicked =
                clickText(
                        "Skip",
                        5
                );
    }

    // -----------------------------------------------------
    // COORDINATE FALLBACK
    // -----------------------------------------------------

    if (!skipClicked) {

        System.out.println(
                "[INFO] SKIP click failed. "
                        + "Trying coordinate fallback..."
        );

        try {

            getDriver().executeScript(
                    "mobile: clickGesture",
                    Map.of(
                            "x", 360,
                            "y", 1166
                    )
            );

            skipClicked = true;

            System.out.println(
                    "[PASS] SKIP clicked using coordinates."
            );

        } catch (Exception e) {

            System.out.println(
                    "[INFO] SKIP coordinate click failed: "
                            + e.getMessage()
            );
        }
    }

    Assert.assertTrue(
            skipClicked,
            "Unable to click SKIP on 30% OFF screen."
    );

    System.out.println(
            "[PASS] 30% OFF screen skipped."
    );

    pause(700);
}

// =========================================================
// DASHBOARD
// =========================================================

private boolean isDashboardDisplayed() {

    String[] dashboardTexts = {
            "Dashboard",
            "Cards",
            "Wallet",
            "Home",
            "My Cards"
    };

    for (String text : dashboardTexts) {

        if (isTextPresent(text, 2)) {

            return true;
        }
    }

    return false;
}

private void verifyDashboard() {

    System.out.println();
    System.out.println(
            "[STEP] Verifying Dashboard"
    );

    boolean dashboard =
            wait(20).until(
                    driver ->
                            isDashboardDisplayed()
            );

    Assert.assertTrue(
            dashboard,
            "Dashboard was not displayed."
    );

    System.out.println(
            "[PASS] Dashboard displayed successfully."
    );
}

// =========================================================
// COMPLETE STARTUP FLOW
// =========================================================

private void runStartupFlow() {

    handleStartupScreen();

    completeOnboarding();

    verifyPremiumScreen();

    selectPremiumPackages();

    closePremiumScreen();

    handleDiscountScreen();

    verifyDashboard();
}

// =========================================================
// TEST
// =========================================================

@Test
public void launchFromSplashToDashboard() {

    System.out.println();
    System.out.println(
            "=================================================="
    );

    System.out.println(
            "[TEST] Card Manager - "
                    + "Launch From Splash To Dashboard"
    );

    System.out.println(
            "=================================================="
    );

    runStartupFlow();

    System.out.println();
    System.out.println(
            "[PASS] COMPLETE STARTUP FLOW PASSED."
    );

    System.out.println(
            "=================================================="
    );
}


}
