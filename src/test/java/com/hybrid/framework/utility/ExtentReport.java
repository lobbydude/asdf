package com.hybrid.framework.utility;

import com.hybrid.framework.config.Constants;
import com.hybrid.framework.config.DriverActions;
import com.hybrid.framework.execution.TestRunner;
import com.hybrid.framework.helpers.Time;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import java.io.IOException;

public class ExtentReport {

    public ExtentReports extent;
    public ExtentTest logger;
    public static String screenShotFileName;

    public ExtentReport(boolean replaceExisting) {
        extent = new ExtentReports(System.getProperty("user.dir") +
                "\\src\\test\\java\\com\\b2b\\automation\\reports\\extent-reporter.html", replaceExisting);
        extent  // Parameter set for more details
                .addSystemInfo("Host Name", "B2B Web Sanity")
                .addSystemInfo("Environment", "UI Automation Testing")
                .addSystemInfo("User Name", System.getProperty("user.name"));
    }

    public void closeExtentReport() {
        if (extent != null) extent.close();
    }

    public void generateReport() {
        extent.endTest(logger);
        extent.flush();
    }

    public void logInfo(String message) {
        logger.log(LogStatus.INFO, message);
    }

    public void logError(String message) {
        logger.log(LogStatus.ERROR, message);
    }

    public void logPass(String message) {
        logger.log(LogStatus.PASS, message);
    }

    public void logFailed(String message) throws IOException {

        logger.log(LogStatus.FAIL, message);

        // Add screenshot to the 'reports' directory.
        screenShotFileName = TestRunner.strTestCaseID + " " + Time.getTimeStampAndDate();
        ScreenShot.capture(DriverActions.globalDriver.getDriver(), screenShotFileName);

        String errorLogMsg = "Error found > Please check the screenshot in : \n"
                + Constants.SCREENSHOT_PATH
                + screenShotFileName + ".png";
        Log.error(errorLogMsg);
        System.out.println(errorLogMsg);
    }
}


