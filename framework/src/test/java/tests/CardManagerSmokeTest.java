package tests;

import driver.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class CardManagerSmokeTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.codelinedynamics.digital.card.mobile.wallet";

    private WebDriverWait appWait(int seconds) {
        return new WebDriverWait(
                getDriver(),
                Duration.ofSeconds(seconds)
        );
    }

    private boolean isTextPresent(String text, int seconds) {

        try {

            appWait(seconds).until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.xpath(
                                    "//*[@text=\"" + text + "\"]"
                            )
                    )
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private boolean clickTextIfPresent(String text, int seconds) {

        try {

            WebElement element = appWait(seconds).until(
                    ExpectedConditions.elementToBeClickable(
                            AppiumBy.xpath(
                                    "//*[@text=\"" + text + "\"]"
                            )
                    )
            );

            element.click();

            System.out.println(
                    "Clicked: " + text
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private void sleep(long milliseconds) {

        try {

            Thread.sleep(milliseconds);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    // ============================================================
    // APP STATE
    // ============================================================

    private void printAppState() {

        AndroidDriver driver = getDriver();

        try {

            System.out.println(
                    "Package: " +
                    driver.getCurrentPackage()
            );

        } catch (Exception ignored) {
        }

        try {

            System.out.println(
                    "Activity: " +
                    driver.currentActivity()
            );

        } catch (Exception ignored) {
        }
    }

    // ============================================================
    // ADMOB HANDLER
    // ============================================================

    private boolean isAdMobDisplayed() {

        try {

            String activity = getDriver().currentActivity();

            return activity != null &&
                    activity.contains("AdActivity");

        } catch (Exception e) {

            return false;
        }
    }

    private void handleAdMob() {

        AndroidDriver driver = getDriver();

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("Checking AdMob");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < 15; i++) {

            if (isAdMobDisplayed()) {

                System.out.println(
                        "AdMob AdActivity detected."
                );

                if (isTextPresent("Test Ad", 2)) {

                    System.out.println(
                            "AdMob Test Ad detected."
                    );
                }

                /*
                 * Allow the advertisement to remain visible
                 * for validation.
                 */
                sleep(5000);

                boolean closed = false;

                String[] closeTexts = {
                        "Close",
                        "Dismiss",
                        "SKIP",
                        "Skip",
                        "×"
                };

                for (String text : closeTexts) {

                    if (clickTextIfPresent(text, 1)) {

                        System.out.println(
                                "AdMob closed using: " + text
                        );

                        closed = true;
                        break;
                    }
                }

                /*
                 * Test ads frequently do not expose the
                 * close button in UiAutomator hierarchy.
                 *
                 * Returning with Android back is therefore
                 * the fallback.
                 */
                if (!closed && isAdMobDisplayed()) {

                    System.out.println(
                            "No native AdMob close button found."
                    );

                    System.out.println(
                            "Returning from AdMob using BACK."
                    );

                    driver.navigate().back();
                }

                sleep(2000);

                System.out.println(
                        "AdMob handling completed."
                );

                return;
            }

            sleep(1000);
        }

        System.out.println(
                "No AdMob AdActivity detected."
        );
    }

    // ============================================================
    // ANDROID PERMISSIONS
    // ============================================================

    private void handlePermissions() {

        System.out.println();
        System.out.println(
                "Checking Android permissions..."
        );

        String[] permissionButtons = {

                "While using the app",
                "Only this time",
                "Allow",
                "ALLOW",
                "While using app"
        };

        for (String button : permissionButtons) {

            try {

                WebElement element =
                        new WebDriverWait(
                                getDriver(),
                                Duration.ofSeconds(2)
                        ).until(
                                ExpectedConditions.elementToBeClickable(
                                        AppiumBy.xpath(
                                                "//*[@text=\"" +
                                                button +
                                                "\"]"
                                        )
                                )
                        );

                element.click();

                System.out.println(
                        "Permission handled: " +
                        button
                );

                sleep(1000);

            } catch (Exception ignored) {
            }
        }
    }

    // ============================================================
    // GENERIC POPUPS
    // ============================================================

    private void handleGenericPopups() {

        String[] buttons = {

                "Close",
                "Dismiss",
                "No thanks",
                "Not now",
                "Cancel"
        };

        for (String button : buttons) {

            clickTextIfPresent(button, 1);
        }
    }

    // ============================================================
    // STARTUP HANDLING
    // ============================================================

    private void handleStartup() {

        AndroidDriver driver = getDriver();

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "STARTING APPLICATION FLOW"
        );
        System.out.println(
                "=============================================="
        );

        driver.activateApp(APP_PACKAGE);

        sleep(3000);

        printAppState();

        /*
         * AdMob can appear a few seconds after application
         * startup, so check multiple times.
         */
        handleAdMob();

        handlePermissions();

        handleGenericPopups();

        sleep(2000);

        /*
         * Ad may appear again after onboarding/startup.
         */
        handleAdMob();

        handlePermissions();
    }

    // ============================================================
    // ONBOARDING
    // ============================================================

    private void processOnboarding() {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "PROCESSING ONBOARDING"
        );
        System.out.println(
                "=============================================="
        );

        /*
         * Let's Start
         */
        if (clickTextIfPresent("Let's Start", 5)) {

            System.out.println(
                    "Let's Start completed."
            );

            sleep(1500);

            handlePermissions();
            handleAdMob();
        }

        /*
         * Language Selection
         */
        if (isTextPresent("Language Selection", 3)) {

            System.out.println(
                    "Language Selection screen detected."
            );

            if (clickTextIfPresent("English", 5)) {

                System.out.println(
                        "English selected."
                );
            }

            sleep(500);

            if (clickTextIfPresent("Done", 5)) {

                System.out.println(
                        "Language selection completed."
                );
            }

            handlePermissions();
            handleAdMob();
        }

        /*
         * Some versions may show English without
         * Language Selection text.
         */
        if (isTextPresent("English", 3)) {

            clickTextIfPresent("English", 3);

            sleep(500);

            clickTextIfPresent("Done", 3);
        }

        /*
         * Process onboarding buttons.
         *
         * We do not depend on the exact intro text.
         * We simply process Next / Done screens.
         */
        for (int i = 0; i < 6; i++) {

            if (clickTextIfPresent("Next", 3)) {

                System.out.println(
                        "Onboarding Next clicked."
                );

                sleep(1000);

                handleAdMob();
                handlePermissions();

                continue;
            }

            if (clickTextIfPresent("Done", 3)) {

                System.out.println(
                        "Onboarding Done clicked."
                );

                sleep(1500);

                handleAdMob();
                handlePermissions();

                break;
            }

            break;
        }

        handleAdMob();
        handlePermissions();
        handleGenericPopups();
    }

    // ============================================================
    // DASHBOARD DETECTION
    // ============================================================

    private boolean dashboardDetected() {

        String[] dashboardTexts = {

                "Card Wallet",
                "My Cards",
                "NFC Reader",
                "Calculator",
                "Card Validator",
                "BIN Validator",
                "Add Card",
                "Add your first Card"
        };

        for (String text : dashboardTexts) {

            if (isTextPresent(text, 2)) {

                System.out.println(
                        "Dashboard element detected: " +
                        text
                );

                return true;
            }
        }

        return false;
    }

    private void waitForDashboard() {

        System.out.println();
        System.out.println(
                "=============================================="
        );
        System.out.println(
                "WAITING FOR DASHBOARD"
        );
        System.out.println(
                "=============================================="
        );

        for (int i = 0; i < 30; i++) {

            handleAdMob();
            handlePermissions();

            if (dashboardDetected()) {

                System.out.println(
                        "Dashboard detected."
                );

                return;
            }

            sleep(1000);
        }

        Assert.fail(
                "Dashboard was not detected."
        );
    }

    // ============================================================
    // FINAL TEST
    // ============================================================

    @Test
    public void launchFromSplashToDashboard() {

        AndroidDriver driver = getDriver();

        System.out.println();
        System.out.println(
                "================================================"
        );
        System.out.println(
                "CARD WALLET FULL E2E AUTOMATION STARTED"
        );
        System.out.println(
                "================================================"
        );

        // STEP 1
        System.out.println(
                "STEP 1: Launching application..."
        );

        handleStartup();

        // STEP 2
        System.out.println(
                "STEP 2: Processing Splash / Onboarding..."
        );

        processOnboarding();

        // STEP 3
        System.out.println(
                "STEP 3: Waiting for Dashboard..."
        );

        waitForDashboard();

        // STEP 4
        System.out.println(
                "STEP 4: Final application verification..."
        );

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Unexpected application package."
        );

        System.out.println();
        System.out.println(
                "================================================"
        );
        System.out.println(
                "CARD WALLET E2E TEST PASSED"
        );
        System.out.println(
                "================================================"
        );

        System.out.println(
                "Application launched successfully."
        );

        System.out.println(
                "AdMob handled successfully."
        );

        System.out.println(
                "Onboarding processed successfully."
        );

        System.out.println(
                "Dashboard reached successfully."
        );

        System.out.println(
                "================================================"
        );
    }
}