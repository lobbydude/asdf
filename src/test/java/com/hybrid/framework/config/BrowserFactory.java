package com.hybrid.framework.config;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {
    public static WebDriver driver = null;

    public static WebDriver startBrowser(String browser) throws MalformedURLException {
        switch (browser) {
            case Constants.CHROME:
                ChromeDriverManager.getInstance().setup();
                ChromeOptions options = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("credentials_enable_service", false);
                prefs.put("password_manager_enabled", false);
                options.addArguments("disable-extensions");
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("chrome.switches", "--disable-extensions");
                options.addArguments("--test-type");
                options.addArguments("disable-infobars");
                options.addArguments("start-maximized");
                DesiredCapabilities cap = DesiredCapabilities.chrome();
                cap.setCapability(ChromeOptions.CAPABILITY, options);
                cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
                return driver = new ChromeDriver(cap);
            case Constants.FIREFOX:
                FirefoxDriverManager.getInstance().setup();
                DesiredCapabilities capabilities = new DesiredCapabilities();
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.startup.homepage","about:blank");
                capabilities.setCapability(FirefoxDriver.PROFILE, profile);
                return driver = new FirefoxDriver(capabilities);
            default:
                throw new IllegalArgumentException("Invalid browser name : " + browser);
        }
    }
}
