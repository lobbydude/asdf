package com.hybrid.framework.helpers;

import org.openqa.selenium.WebDriver;

public class GlobalWebDriver {

    private WebDriver driver;

    public GlobalWebDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
