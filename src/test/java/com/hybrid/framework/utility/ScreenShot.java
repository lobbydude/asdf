package com.hybrid.framework.utility;

import com.hybrid.framework.config.Constants;
import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenShot {

    public static String capture(WebDriver driver, String screenShotName) throws IOException {

        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = StringBuilderUtils.build(Constants.SCREENSHOT_PATH, screenShotName, ".png");
        File destination = new File(dest);
        Files.copy(source, destination);

        return dest;
    }
}
