package com.hybrid.framework.config;

import com.hybrid.framework.execution.TestRunner;
import com.hybrid.framework.helpers.*;
import com.hybrid.framework.utility.Log;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.io.IOException;

/**
 * Class containing all the keywords that can be
 * used along the UI interaction and inputted in Excel data engine.
 */
public class DriverActions {

    public static WebDriver driver;
    public static Wait wait;
    public static JSExecutor js;
    public static ElementActions elementActions;
    public static GlobalWebDriver globalDriver;

    /**
     * Browser actions.
     */

    public static void openBrowser(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Opening Browser");

            if (data == null) {
                TestRunner.extentReport.logFailed("Cannot open a browser without defined one");
                return;
            }

            if (data.equalsIgnoreCase("firefox")) {
                driver = BrowserFactory.startBrowser(data);
            }
            else if (data.equalsIgnoreCase("chrome")) {
                driver = BrowserFactory.startBrowser(data);
            }

            driver.manage().window().maximize();
            globalDriver = new GlobalWebDriver(driver);
            wait = new Wait(driver);
            js = new JSExecutor(driver);
            elementActions = new ElementActions(driver);
            TestRunner.extentReport.logInfo("Browser was opened");

        } catch (Exception e) {
            Log.error("Not able to open the browser : " + data + " | " + e.getMessage());
        }
    }

    public static void closeBrowser(String object, String locator, String data) throws IOException {
        try {
            if (globalDriver.getDriver() == null) {
                return;
            }
            globalDriver.getDriver().quit();
            globalDriver = null;
            TestRunner.extentReport.logInfo("Closed the browser");
        } catch (Exception e) {
            Log.error("Not able to Close the Browser : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    /**
     * Browser redirection actions.
     */

    public static void goBack(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Going back to the previous page");
            globalDriver.getDriver().navigate().back();
            wait.impWait(10);
            TestRunner.extentReport.logPass("Back to the previous page was successfully performed");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to go back to the previous page");
            TestRunner.boolResult = false;
        }
    }

    public static void goForward(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Going forward from this page : " + driver.getCurrentUrl());
            globalDriver.getDriver().navigate().forward();
            wait.impWait(10);
            TestRunner.extentReport.logPass("Browse forward was successfully performed");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to go to target page");
            TestRunner.boolResult = false;
        }
    }

    public static void navigate(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Navigating to URL");
            wait.impWait(10);
            globalDriver.getDriver().get(data);
            TestRunner.extentReport.logPass("Navigated to " + data);
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to navigate the url : " + data + " | " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void refresh(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Refreshing this page : " + globalDriver.getDriver().getCurrentUrl());
            globalDriver.getDriver().navigate().refresh();
            wait.impWait(10);
            TestRunner.extentReport.logPass("Refreshed");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to go to refresh the page : " + globalDriver.getDriver().getCurrentUrl());
            TestRunner.boolResult = false;
        }
    }

    /**
     * Element actions.
     */

    public static void click(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Waiting until " + object + " is clickable");
            wait.untilElementIsClickable(Elements.getElement(locator, TestRunner.OR.getString(object)));
            TestRunner.extentReport.logInfo("Wait succeeded and will click " + object);
            Elements.getElement(locator, TestRunner.OR.getString(object)).click();
            TestRunner.extentReport.logPass("Click for " + object + " was successfully performed");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to click the object name : " + object + " | " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void input(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Entering the text in : " + object);
            Elements.getElement(locator, TestRunner.OR.getString(object)).sendKeys(data);
            wait.impWait(10);
            TestRunner.extentReport.logPass("Inputted " + data + " to " + object);
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to enter " + data + " : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void inputAndEnter(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Entering the text in : " + object);
            Elements.getElement(locator, TestRunner.OR.getString(object)).sendKeys(data);
            wait.impWait(10);
            Elements.getElement(locator, TestRunner.OR.getString(object)).sendKeys(Keys.ENTER);
            TestRunner.extentReport.logPass("Input and enter " + data + " to " + object + " successfully");
        }
        catch(Exception e) {
            TestRunner.extentReport.logFailed("Not able to enter and submit :" + data + " : "+ e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void selectByVisibleText(String object, String locator, String data) throws IOException {
        try {
            Select select = new Select(Elements.getElement(locator, TestRunner.OR.getString(object)));
            select.selectByVisibleText(data);
            wait.impWait(10);
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to select the text " + data + " in the dropdown : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    /**
     * Mouse hover actions.
     */

    public static void moveToElement(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Moving to the element : " + object);
            elementActions.moveToElement(Elements.getElement(locator, TestRunner.OR.getString(object)));
            TestRunner.extentReport.logPass("Moved the mouse to " + object);
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to move to element : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    /**
     * Wait actions.
     */

    public static void waitFor(String object, String locator, String data) throws IOException {
        // Force the machine to pause for given time (data param) seconds, otherwise will pause for 5 seconds.
        try {
            TestRunner.extentReport.logInfo("Performing " + data + " seconds delay");

            if (data != null) {
                Thread.sleep(Integer.parseInt(data) * 1000);
            } else {
                Thread.sleep(5000);
            }

            TestRunner.extentReport.logPass("Waited successfully");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to wait : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void waitExplicitly(String object, String locator, String data) throws IOException {
        try {
            if (object == null && locator == null && data == null) {
                TestRunner.extentReport.logFailed("Cannot wait explicitly without complete parameters.");
                return;
            }

            TestRunner.extentReport.logInfo("Waiting for " + object + " explicitly in " + data + " seconds");
            wait.untilElementVisible(Elements.getElement(locator, TestRunner.OR.getString(object)));
            wait.impWait(Integer.parseInt(data));
            TestRunner.extentReport.logPass("Waited successfully within " + data + " seconds");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to wait explicitly : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void waitForElementInvisibility(String object, String locator, String data) throws IOException {
        try {
            if (object == null && locator == null && data == null) {
                TestRunner.extentReport.logFailed("Cannot wait explicitly without complete parameters.");
                return;
            }

            TestRunner.extentReport.logInfo("Waiting for " + object + " to be invisible within " + data + " seconds");
            wait.untilElementNotVisible(Elements.getBy(locator, TestRunner.OR.getString(object)));
            TestRunner.extentReport.logPass("Object " + object + " has been invisible");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Not able to wait : " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    /**
     * Assertion actions.
     */

    public static void assertElementVisible(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Asserting if element is visible.");
            Assert.assertTrue(Elements.getElement(locator, TestRunner.OR.getString(object)).isDisplayed());
            TestRunner.extentReport.logPass("Assertion : Object " + object + " was visible");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion : Object " + object + " is not visible | " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void assertPageReached(String object, String locator, String data) throws IOException {
        try {
            TestRunner.extentReport.logInfo("Asserting if page was reached");
            Assert.assertTrue(driver.getCurrentUrl().contains(data));
            TestRunner.extentReport.logPass("Assertion : Target page was reached");
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion error. Page was not reached." + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void assertAllResultsHaveHQIcon(String object, String locator, String data) throws IOException {
        int amountOfResults = Elements.getElements(locator, TestRunner.OR.getString(object)).size();
        try {
            int amountOfResultsWithHQ = 0;
            TestRunner.extentReport.logInfo("Asserting if all the results have HQ icon");
            for (int result = 0; result < amountOfResults; result++) {
                Elements.getElement(locator, "//*[@id='grid']/tbody/tr[" + (result + 1) + "]/td[8]/a/i");
                amountOfResultsWithHQ++;
            }
            if (amountOfResults == amountOfResultsWithHQ) {
                TestRunner.extentReport.logPass("Assertion : All results have HQ icon");
            }
            else {
                TestRunner.extentReport.logFailed("Assertion : There are results that have no HQ icon");
                TestRunner.boolResult = false;
            }

        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion : There was an error in counting the results | " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void assertHasResults(String object, String locator, String data) throws IOException {
        try {
            int amountOfResults = Elements.getElements(locator, TestRunner.OR.getString(object)).size();
            TestRunner.extentReport.logInfo("Asserting if there is/are result(s)");

            if (amountOfResults > 0) {
                TestRunner.extentReport.logPass("Assertion : There are result(s)");
            }
            else {
                TestRunner.extentReport.logFailed("Assertion : There are no result(s)");
                TestRunner.boolResult = false;
            }
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion : There was an error in counting the results > " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void assertNoResults(String object, String locator, String data) throws IOException {
        try {
            String strResultMsg = Elements.getElement(locator, TestRunner.OR.getString(object)).getText();
            TestRunner.extentReport.logInfo("Asserting if there are no result(s)");

            if (strResultMsg.contains("we don't have data")) {
                TestRunner.extentReport.logPass("Assertion : There are no result(s)");
            }
            else {
                TestRunner.extentReport.logFailed("Assertion : There are result(s)");
                TestRunner.boolResult = false;
            }
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion : There was an error in counting the results > " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static void assertNotIncludedInSubs(String object, String locator, String data) throws IOException {
        try {
            String strResultMsg = Elements.getElement(locator, TestRunner.OR.getString(object)).getText();
            TestRunner.extentReport.logInfo("Asserting if there are no included result(s)");

            if (strResultMsg.contains("To access these")) {
                TestRunner.extentReport.logPass("Assertion : There are no included result(s)");
            }
            else {
                TestRunner.extentReport.logFailed("Assertion : There are included result(s)");
                TestRunner.boolResult = false;
            }
        } catch (Exception e) {
            TestRunner.extentReport.logFailed("Assertion : There was an error in counting the results > " + e.getMessage());
            TestRunner.boolResult = false;
        }
    }
}
