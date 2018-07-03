package com.hybrid.framework.config;

/**
 * Contains all callable constant values.
 */
public class Constants {
    // Runmode
    public static final String YES = "yes";

    // Method name
    public static final String NAVIGATE = "navigate";

    // Browsers
    public static final String FIREFOX = "firefox";
    public static final String CHROME = "chrome";
    public static final String IE = "ie";
    public static final String EDGE = "edge";
    public static final String PHANTOMJS = "phantomjs";
    public static final String HTMLUNIT = "htmlunit";

    // Elements
    public static final String LOCATOR_ID = "id";
    public static final String LOCATOR_NAME = "name";
    public static final String LOCATOR_CSS = "css";
    public static final String LOCATOR_XPATH = "xpath";

    // System variables
    public static final String KEYWORD_PASS = "PASS";
    public static final String KEYWORD_FAIL = "FAIL";

    // CSV File Paths
    public static final String SETTINGS_PATH = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/Settings.csv";
    public static final String TEST_CASES_PATH = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/Test Cases.csv";
    public static final String TEST_STEPS_PATH = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/Test Steps.csv";

    // URLs
    public static final String BASE_URL = "http://192.168.12.33/Tax_Test/Login.aspx";

    // Excel file FILE_FORMAT
    private static final String FILE_FORMAT = ".xlsx";

    // QA user names
  public static final String JAMES = "jmarturillas";
    public static final String CAR = "maricar.marzan";
    public static final String GERARD = "gerard.deguito";
    public static final String RON = "ron.delmoro";

    public static final String EXCEL_NAME_MASTER = "DataEngine";
    public static final String EXCEL_NAME_CAR = "DataEngine_Car";
    public static final String EXCEL_NAME_GERARD = "DataEngine_Gerard";
    public static final String EXCEL_NAME_RON = "DataEngine_Ron";

    // Excel path
    public static final String EXCEL_PATH = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/" + EXCEL_NAME_MASTER + FILE_FORMAT;
    public static final String EXCEL_CAR = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/" + EXCEL_NAME_CAR + FILE_FORMAT;
    public static final String EXCEL_GERARD = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/" + EXCEL_NAME_GERARD + FILE_FORMAT;
    public static final String EXCEL_RON = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/dataEngine/" + EXCEL_NAME_RON + FILE_FORMAT;

    // Screenshot path
    public static final String SCREENSHOT_PATH = System.getProperty("user.dir") + "/src/test/java/com/hybrid/framework/reports/screenshots/";

    //Data Sheet Column Numbers

    // Test Case sheet
    public static final int COL_TESTCASEID = 0;

    public static final int COL_CHROME_RUNMODE = 2;
    public static final int COL_RESULT_CHROME = 3;
    public static final int COL_TIMEANDDATE_CHROME = 4;

    public static final int COL_FIREFOX_RUNMODE = 5;
    public static final int COL_RESULT_FIREFOX = 6;
    public static final int COL_TIMEANDDATE_FIREFOX = 7;

    // Test Step sheet
    public static final int COL_PAGEOBJECT = 4;
    public static final int COL_LOCATOR = 5;
    public static final int COL_DRIVERACTIONS = 6;
    public static final int COL_DATASET = 7;
    public static final int COL_TESTSTEP_CHROME_RESULT = 8;
    public static final int COL_TESTSTEP_CHROME_TIMEANDDATE = 9;
    public static final int COL_TESTSTEP_FIREFOX_RESULT = 10;
    public static final int COL_TESTSTEP_FIREFOX_TIMEANDDATE = 11;

    // Data Engine Excel sheets
    public static final String SHEET_TESTSTEPS = "Test Steps";
    public static final String SHEET_TESTCASES = "Test Cases";

    // Browser binaries
    public static final String CHROMEDRIVER = System.getProperty("user.dir") + "/src/test/resources/browser_binaries/chromedriver.exe";
    public static final String FIREFOXDRIVER = System.getProperty("user.dir") + "/src/test/resources/browser_binaries/geckodriver.exe";

    // Error Message
    public static final String ERR_MSG = "Check the 'Test Steps' sheet in Excel and/or 'extent-reporter.html'";
}