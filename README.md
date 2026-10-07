# Android Appium Automation Framework

## Overview

A production-style Android mobile automation framework built with **Java, Appium, UiAutomator2, TestNG, and Maven**.

This project automates and validates a complete **Card Manager mobile application startup journey**, from the initial app launch through onboarding, advertisement handling, premium plan interactions, promotional offers, wallet activation, and final dashboard verification.

The framework is designed with reusable driver management, explicit waits, dynamic element handling, mobile gestures, fallback strategies, assertions, and maintainable test structure.

---

## Project Objective

The objective of this project is to demonstrate end-to-end Android UI automation for a real mobile application while handling common challenges encountered in mobile QA automation, including:

* Dynamic UI elements
* Scrollable screens
* Advertisement screens
* Non-clickable text elements with clickable parent containers
* Dynamic screen transitions
* Android UI hierarchy variations
* Coordinate-based fallback interactions
* Explicit synchronization and waits
* End-to-end assertions
* Real Android device execution

---

## Technology Stack

| Technology          | Purpose                           |
| ------------------- | --------------------------------- |
| Java 17             | Programming language              |
| Appium 3            | Mobile automation                 |
| UiAutomator2        | Android automation driver         |
| TestNG              | Test execution and assertions     |
| Maven               | Build and dependency management   |
| Android             | Mobile platform                   |
| Real Android Device | Test execution                    |
| XPath               | Dynamic element identification    |
| Mobile Gestures     | Scroll and interaction automation |

---

## Automated End-to-End Flow

The primary smoke test covers the following journey:

```text
Application Launch
        ↓
App Open Advertisement
        ↓
Continue to App
        ↓
Language Selection
        ↓
English
        ↓
Language Confirmation
        ↓
Onboarding Screen 1
        ↓
Onboarding Screen 2
        ↓
Onboarding Screen 3
        ↓
Done
        ↓
Interstitial Advertisement
        ↓
Close Advertisement
        ↓
Premium Screen
        ↓
Verify Monthly
        ↓
Verify Yearly
        ↓
Verify LifeTime
        ↓
Select Monthly
        ↓
Select Yearly
        ↓
Select LifeTime
        ↓
Close Premium Screen
        ↓
30% OFF Promotion
        ↓
Select Monthly
        ↓
Select Yearly
        ↓
Activate Your Wallet
        ↓
Handle Confirmation / Popup
        ↓
Skip
        ↓
Dashboard
```

---

## Key Automation Coverage

### Application Startup

* Detects the initial application state
* Handles App Open Advertisement
* Handles "Continue to app"
* Handles startup transitions
* Supports dynamic waiting instead of relying only on fixed delays

### Language Selection

* Detects the Languages screen
* Selects English
* Confirms language selection
* Uses element-based interaction with coordinate fallback where required

### Onboarding

Automates all three onboarding stages:

* Onboarding Screen 1
* Onboarding Screen 2
* Onboarding Screen 3
* Next / Skip handling
* Done button validation

### Advertisement Handling

The framework handles:

* App Open Ads
* Interstitial Ads
* Close button detection
* Content-description based locators
* Coordinate fallback when required
* Post-ad screen validation

### Premium Plans

The automation validates and interacts with:

* Monthly
* Yearly
* LifeTime
* Activate Your Wallet

The Premium screen contains a scrollable area, so the framework includes dedicated scrolling logic to locate elements that are initially outside the visible viewport.

### Promotional Offer

The 30% OFF screen is also automated:

* Monthly selection
* Yearly selection
* Activate Your Wallet
* Post-activation popup/screen handling
* SKIP interaction
* Dashboard navigation

### Dashboard Validation

The final stage verifies that the application successfully reaches the main application area after completing the complete startup journey.

---

## Framework Features

### Reusable Driver Management

The test extends a common `BaseTest` class responsible for:

* AndroidDriver initialization
* Test setup
* Test teardown
* Driver lifecycle management

### Explicit Waits

The framework uses `WebDriverWait` and Selenium expected conditions to synchronize interactions with the application UI.

This helps reduce failures caused by:

* Slow screen transitions
* Delayed UI rendering
* Advertisement loading
* Dynamic element availability

### Dynamic Element Handling

The framework uses multiple locator strategies, including:

* XPath
* Text-based identification
* Content-description
* Clickable parent identification
* Bounds-based identification

### Click Fallback Strategy

For difficult mobile elements, the framework uses a layered approach:

```text
Primary XPath
      ↓
Clickable Parent
      ↓
Text Locator
      ↓
Content Description
      ↓
Coordinate Fallback
```

This makes the automation more resilient to UI hierarchy differences.

### Mobile Scroll Automation

The framework uses Appium mobile gestures to interact with scrollable screens.

Scrolling is implemented for:

* Premium plans
* LifeTime package
* 30% OFF promotional screen
* SKIP and wallet activation controls

### Assertions

TestNG assertions are used to validate important checkpoints throughout the flow rather than simply performing clicks.

---

## Project Structure

```text
Android-Automation/
│
├── apps/
│   └── CardManager/
│       ├── tests/
│       │   └── CardManagerSmokeTest.java
│       │
│       └── screens/
│
├── framework/
│   └── src/
│       └── main/
│
├── pom.xml
├── testng.xml
└── .gitignore
```

---

## Test Architecture

The framework follows a reusable test architecture where common functionality is separated from the actual test scenario.

```text
CardManagerSmokeTest
        │
        ▼
     BaseTest
        │
        ▼
   Driver Management
        │
        ▼
    AndroidDriver
        │
        ▼
     Appium Server
        │
        ▼
   Android Device
```

---

## Test Execution

### Prerequisites

Install and configure:

* Java 17
* Maven
* Node.js
* Appium
* UiAutomator2 Driver
* Android SDK / ADB
* Android device or emulator

Verify the environment:

```bash
java -version
mvn -version
node -version
appium --version
adb devices
```

### Start Appium Server

Start the Appium server locally:

```bash
appium
```

Default server:

```text
http://127.0.0.1:4723/
```

### Run the Test

From the project root:

```bash
mvn clean test
```

The TestNG test suite will execute the Android automation flow.

---

## Test Scenario

### Test Name

`launchFromSplashToDashboard`

### Validation Goal

Verify that a user can successfully progress from the initial application launch to the application dashboard through the complete startup and promotional flow.

### Expected Result

The test passes when the application successfully reaches and displays the Dashboard after completing the automated startup journey.

---

## Mobile Automation Challenges Addressed

This project demonstrates practical handling of several real-world mobile automation challenges.

### 1. Scrollable UI

Some controls are outside the initial viewport.

**Solution:** Appium `mobile: scrollGesture` with controlled scrolling and visibility checks.

### 2. Text Element Is Not Clickable

In some screens, visible text is represented by a non-clickable child element while its parent container is clickable.

**Solution:** Locate the text and identify its nearest clickable ancestor.

### 3. Dynamic Advertisement Screens

Advertisement activities can appear between application screens.

**Solution:** Detect advertisement-specific elements and wait for the appropriate close/continue controls.

### 4. UI Hierarchy Variations

Mobile UI hierarchy can change depending on screen state.

**Solution:** Use multiple locator strategies and controlled fallback mechanisms.

### 5. Synchronization

Mobile screens may require different loading times.

**Solution:** Explicit waits and state-based validation instead of depending exclusively on static sleeps.

---

## Quality Engineering Practices Demonstrated

This project demonstrates practical QA automation skills including:

* End-to-end test automation
* Smoke testing
* UI validation
* Functional validation
* Mobile application testing
* Test synchronization
* Dynamic element handling
* Gesture automation
* TestNG assertions
* Reusable automation utilities
* Driver lifecycle management
* Failure-resistant locator strategies
* Maven-based test execution
* Real Android device testing

---

## Future Enhancements

Potential future improvements include:

* Page Object Model expansion
* Additional functional test scenarios
* Data-driven testing
* Parallel device execution
* Cross-device testing
* Android version coverage
* Allure or Extent reporting
* Screenshot capture on failure
* Automatic video recording
* CI/CD integration
* BrowserStack device execution
* API integration for test data management

---

## Disclaimer

This repository is intended for **QA automation demonstration and portfolio purposes**.

Application-specific identifiers and configuration values should be provided through local configuration and environment-specific settings rather than exposing sensitive or device-specific information in source control.

---

## Author

**Hasnain Raza**

**QA Engineer | AI, Manual & Automation | SaaS, Web & Mobile**

Specializing in:

* Manual QA
* Web Automation
* Mobile Automation
* API Testing
* Playwright
* Appium
* TestNG
* SaaS Testing
* AI Application Testing

---

## Repository

**Android Appium Automation Framework**

https://github.com/softwarepro465/android-appium-automation-framework
