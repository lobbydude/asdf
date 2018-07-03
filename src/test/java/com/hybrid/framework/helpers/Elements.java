package com.hybrid.framework.helpers;

import com.hybrid.framework.config.Constants;
import com.hybrid.framework.config.DriverActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Class storage for parsing web element(s).
 * Ability to return required element.
 */
public class Elements {

    public static WebElement getElement(String locator, String value) {
        WebElement element;
        switch (locator) {
            case Constants.LOCATOR_ID :
                element = DriverActions.globalDriver.getDriver().findElement(By.id(value));
                break;
            case Constants.LOCATOR_NAME :
                element = DriverActions.globalDriver.getDriver().findElement(By.name(value));
                break;
            case Constants.LOCATOR_CSS :
                element = DriverActions.globalDriver.getDriver().findElement(By.cssSelector(value));
                break;
            default :
                element = DriverActions.globalDriver.getDriver().findElement(By.xpath(value));
        }
        return element;
    }

    public static List<WebElement> getElements(String locator, String value) {
        List<WebElement> elements;
        switch (locator) {
            case Constants.LOCATOR_ID :
                elements = DriverActions.globalDriver.getDriver().findElements(By.id(value));
                break;
            case Constants.LOCATOR_NAME :
                elements = DriverActions.globalDriver.getDriver().findElements(By.name(value));
                break;
            case Constants.LOCATOR_CSS :
                elements = DriverActions.globalDriver.getDriver().findElements(By.cssSelector(value));
                break;
            default :
                elements = DriverActions.globalDriver.getDriver().findElements(By.xpath(value));
        }
        return elements;
    }

    public static By getBy(String locator, String value) {
        By by;
        switch (locator) {
            case Constants.LOCATOR_ID :
                by = By.id(value);
                break;
            case Constants.LOCATOR_NAME :
                by = By.name(value);
                break;
            case Constants.LOCATOR_CSS :
                by = By.cssSelector(value);
                break;
            default :
                by = By.xpath(value);
        }
        return by;
    }
}
