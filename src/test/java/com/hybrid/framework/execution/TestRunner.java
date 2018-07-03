package com.hybrid.framework.execution;

import com.hybrid.framework.config.Constants;
import com.hybrid.framework.config.DriverActions;
import com.hybrid.framework.helpers.Time;
import com.hybrid.framework.utility.BrowserSelector;
import com.hybrid.framework.utility.ExcelUtils;
import com.hybrid.framework.utility.ExtentReport;
import com.hybrid.framework.utility.StringBuilderUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Class in-charge of executing all the keywords
 * inputted in the OR (Object Repository) file.
 */

public class TestRunner {

    public static ResourceBundle OR;
    public static String strTestCaseID;

    private static DriverActions driverActions;
    private static String strDriverAction;
    private static String strPageObject;
    private static String strLocator;
    private static String strRunMode;
    private static String strData;
    private static Method methods[];

    private static int intTestStep;
    private static int intTestLastStep;
    private static int methodTimesRun;
    private static int tcResultColumn = 0;
    private static int tcTestPerformedColumn = 0;
    private static int tsResultColumn = 0;
    private static int tsTestPerformedColumn = 0;

    private static String excelPath = null;
    private static String[] excelPaths = null;
    private static String excelName = null;
    private static String[] excelNames = null;
    private static String browserToRun = null;

    public static boolean boolResult;

    public static ExtentReport extentReport;

    /**
     * Class construct that initiates all the class(es) and method(s) of required test case(s) to run.
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public TestRunner() throws NoSuchMethodException, SecurityException {
        driverActions = new DriverActions();
        methods = driverActions.getClass().getDeclaredMethods();
    }

    /**
     * performTest() :
     *  main method to start all the run.
     *  Annotated by @Test as TestNG method.
     *
     * @throws Exception
     */
    @Test
    public static void performTest() throws Exception {
        // Select the workbook via machine username
        ExcelUtils.wbLoopAmountSetter();

        // Configure the logger at log4j.xml file.
        DOMConfigurator.configure("log4j.xml");

        // Prepare the .properties file
        OR = ResourceBundle.getBundle("OR");

        // Instantiate the test runner.
        TestRunner startEngine = new TestRunner();
        startEngine.executeTestCase();
    }

    /**
     * executeTestCase() :
     *  in-charge of executing the test case(s) regardless of its number (amount).
     *
     * @throws Exception
     */

    private void executeTestCase() throws Exception {
        int intTotalTestCases;
        int intTestCase;
        int i;
        int y = 0;

        // Runmodes declaration
        List<Integer> browserColumns = new ArrayList<>();
        browserColumns.add(Constants.COL_CHROME_RUNMODE);
        browserColumns.add(Constants.COL_FIREFOX_RUNMODE);

        // Excel sheet paths
        List<String> wbPaths = new ArrayList<>();
        wbPaths.add(Constants.EXCEL_PATH);
        wbPaths.add(Constants.EXCEL_CAR);
        wbPaths.add(Constants.EXCEL_GERARD);
        wbPaths.add(Constants.EXCEL_RON);

        for (int wbLoop = 0; wbLoop < ExcelUtils.numberOfWbToSet; wbLoop++) {
            configureExcel(wbPaths, wbLoop);
            for (int browserColumn : browserColumns) {
                setBrowserToRun(browserColumn);

                // Counts the test case(s) in the 'Test Cases' sheet in 'DataEngine.xlsx'
                intTotalTestCases = ExcelUtils.getRowCount(Constants.SHEET_TESTCASES);
                intTestCase = 1;
                i = 1;

                if (intTotalTestCases > 1) methodTimesRun = 1;

                setSheetColumnsToBeUsed(browserColumn);

                // Execute each test case through a loop.
                for(; intTestCase < intTotalTestCases; intTestCase++) {

                    // Extract the 'Test Case ID' and 'Runmode' values in the sheet 'Test Cases'
                    strTestCaseID = ExcelUtils.getCellData(intTestCase, Constants.COL_TESTCASEID, Constants.SHEET_TESTCASES);
                    strRunMode = ExcelUtils.getCellData(intTestCase, browserColumn, Constants.SHEET_TESTCASES);

                    // Run the test case if its 'Runmode' is set to 'Yes'
                    if (strRunMode.equalsIgnoreCase(Constants.YES)) {
                        if (i > 1 || y > 0)
                            extentReport = new ExtentReport(false);
                        else
                            extentReport = new ExtentReport(true);
                            y++;

                        // Start ExtentReporting engine.
                        if (ExcelUtils.numberOfWbToSet == 1)
                            extentReport.logger = extentReport.extent.startTest
                                    (StringBuilderUtils.build("(", browserToRun, ") ", strTestCaseID, " from ", excelName));
                        else
                            extentReport.logger = extentReport.extent.startTest
                                    (StringBuilderUtils.build("(", browserToRun, ") ", strTestCaseID, " from ", excelNames[wbLoop]));

                        // Get the starting row and last row of the specified test case.
                        intTestStep = ExcelUtils.getRowContains(strTestCaseID, Constants.COL_TESTCASEID, Constants.SHEET_TESTSTEPS);
                        intTestLastStep = ExcelUtils.getTestStepsCount(Constants.SHEET_TESTSTEPS, strTestCaseID, intTestStep);

                        // Step invocation is initiated as 'true'; will change to 'false' once error was found at runtime.
                        boolResult = true;

                        // Skip reading the description by adding the count of 'intTestStep'
                        if (intTestStep < ExcelUtils.getRowCount(Constants.SHEET_TESTSTEPS)) intTestStep++;

                        // Invoke the browser.
                        if (methodTimesRun == 1) BrowserSelector.invokeBrowser(browserColumn);

                        // Execute each step.
                        for (; intTestStep < intTestLastStep; intTestStep++) {
                            // Extract the action keyword from the sheet 'Test Steps' column 'Action Keyword'
                            strDriverAction = ExcelUtils.getCellData(intTestStep, Constants.COL_DRIVERACTIONS,Constants.SHEET_TESTSTEPS);

                            // Extract the page object from the sheet 'Test Steps' column 'Page Object'
                            strPageObject = ExcelUtils.getCellData(intTestStep, Constants.COL_PAGEOBJECT, Constants.SHEET_TESTSTEPS);
                            strLocator = ExcelUtils.getCellData(intTestStep, Constants.COL_LOCATOR, Constants.SHEET_TESTSTEPS);
                            strData = ExcelUtils.getCellData(intTestStep, Constants.COL_DATASET, Constants.SHEET_TESTSTEPS);

                            // Invoke execution of each step.
                            executeTestStepActions();

                            if(!boolResult) {
                                setTestCaseResultInExcel(boolResult, intTestCase, wbLoop);
                                break;
                            }
                        }

                        if (boolResult) setTestCaseResultInExcel(boolResult, intTestCase, wbLoop);

                        // Generate report.
                        extentReport.generateReport();
                        i++;
                    }
                }
                // Close the browser if test case(s) is/are all covered
                if (intTestCase == intTotalTestCases) {
                    if (extentReport != null)
                        closeBrowser();
                        methodTimesRun = 1;
                }
            }
        }
        // Close the stream.
        if (extentReport != null) extentReport.closeExtentReport();
    }

    /**
     * executeTestStepActions() :
     *  in-charge of enumerating all the test steps
     *  indicated from the DataEngine.xlsx data source
     *
     * @throws Exception
     */
    private static void executeTestStepActions() throws Exception {
        for (Method method : methods) {
            if(method.getName().equals(strDriverAction)) {
                navigateBaseUrl(method.getName(), methodTimesRun);

                // Invokes the method indicated in the 'Test Steps' sheet of 'DataEngine.xlsx'
                method.invoke(driverActions, strPageObject, strLocator, strData);

                methodTimesRun++;

                if(boolResult) {
                    ExcelUtils.setCellData(Constants.KEYWORD_PASS, intTestStep, tsResultColumn, Constants.SHEET_TESTSTEPS, excelPath);
                    ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestStep, tsTestPerformedColumn, Constants.SHEET_TESTSTEPS, excelPath);
                    break;
                }
                else {
                    System.out.println(Constants.ERR_MSG);
                    ExcelUtils.setCellData(Constants.KEYWORD_FAIL, intTestStep, Constants.COL_TESTSTEP_CHROME_RESULT, Constants.SHEET_TESTSTEPS, excelPath);
                    ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestStep, Constants.COL_TESTSTEP_CHROME_TIMEANDDATE, Constants.SHEET_TESTSTEPS, excelPath);
                    DriverActions.closeBrowser(null, null, null);
                    break;
                }
            }
        }
    }

    /**
     * Methods used inside this very class
     */

    private static void navigateBaseUrl(String methodName, int timesRun) throws IOException {
        if (timesRun == 1) {
            if (!Objects.equals(methodName, Constants.NAVIGATE)) {
                DriverActions.navigate(null, null, Constants.BASE_URL);
            }
        }
    }

    private static void closeBrowser() throws IOException {
        DriverActions.closeBrowser(null, null, null);
    }

    private static void setTestCaseResultInExcel(boolean boolResult, int intTestCase, int wbLoop) {
        if (!boolResult) {
            if (ExcelUtils.numberOfWbToSet == 1) {
                ExcelUtils.setCellData(Constants.KEYWORD_FAIL, intTestCase, tcResultColumn, Constants.SHEET_TESTCASES, excelPath);
                ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestCase, tcTestPerformedColumn, Constants.SHEET_TESTCASES, excelPath);
            }
            else {
                ExcelUtils.setCellData(Constants.KEYWORD_FAIL, intTestCase, tcResultColumn, Constants.SHEET_TESTCASES, excelPaths[wbLoop]);
                ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestCase, tcTestPerformedColumn, Constants.SHEET_TESTCASES, excelPaths[wbLoop]);
            }
        }
        else if (boolResult){
            if (ExcelUtils.numberOfWbToSet == 1) {
                ExcelUtils.setCellData(Constants.KEYWORD_PASS, intTestCase, tcResultColumn, Constants.SHEET_TESTCASES, excelPath);
                ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestCase, tcTestPerformedColumn, Constants.SHEET_TESTCASES, excelPath);
            }
            else {
                ExcelUtils.setCellData(Constants.KEYWORD_PASS, intTestCase, tcResultColumn, Constants.SHEET_TESTCASES, excelPaths[wbLoop]);
                ExcelUtils.setCellData(Time.getCurrentTimeAndDate(), intTestCase, tcTestPerformedColumn, Constants.SHEET_TESTCASES, excelPaths[wbLoop]);
            }
        }
    }

    private static void setSheetColumnsToBeUsed(int browserColumn) {
        if (browserColumn == Constants.COL_CHROME_RUNMODE) {
            tcResultColumn = Constants.COL_RESULT_CHROME;
            tcTestPerformedColumn = Constants.COL_TIMEANDDATE_CHROME;

            tsResultColumn = Constants.COL_TESTSTEP_CHROME_RESULT;
            tsTestPerformedColumn = Constants.COL_TESTSTEP_CHROME_TIMEANDDATE;
        }
        else if (browserColumn == Constants.COL_FIREFOX_RUNMODE) {
            tcResultColumn = Constants.COL_RESULT_FIREFOX;
            tcTestPerformedColumn = Constants.COL_TIMEANDDATE_FIREFOX;

            tsResultColumn = Constants.COL_TESTSTEP_FIREFOX_RESULT;
            tsTestPerformedColumn = Constants.COL_TESTSTEP_FIREFOX_TIMEANDDATE;
        }
    }

    private static void setBrowserToRun(int browserColumn) {
        if (browserColumn == Constants.COL_CHROME_RUNMODE)
            browserToRun = Constants.CHROME;
        else if (browserColumn == Constants.COL_FIREFOX_RUNMODE)
            browserToRun = Constants.FIREFOX;
    }

    private static void configureExcel(List<String> wbPaths, int wbLoop) {
        if (ExcelUtils.machineName.equalsIgnoreCase(Constants.JAMES)) {
            ExcelUtils.setExcelFile(wbPaths.get(0));
            excelPath = Constants.EXCEL_PATH;
            excelName = Constants.EXCEL_NAME_MASTER;
        }
        else if (ExcelUtils.machineName.equalsIgnoreCase(Constants.CAR)) {
            ExcelUtils.setExcelFile(wbPaths.get(1));
            excelPath = Constants.EXCEL_CAR;
            excelName = Constants.EXCEL_NAME_CAR;
        }
        else if (ExcelUtils.machineName.equalsIgnoreCase(Constants.GERARD)) {
            ExcelUtils.setExcelFile(wbPaths.get(2));
            excelPath = Constants.EXCEL_GERARD;
            excelName = Constants.EXCEL_NAME_GERARD;
        }
        else if (ExcelUtils.machineName.equalsIgnoreCase(Constants.RON)) {
            ExcelUtils.setExcelFile(wbPaths.get(3));
            excelPath = Constants.EXCEL_RON;
            excelName = Constants.EXCEL_NAME_RON;
        }
        else {
            ExcelUtils.setExcelFile(wbPaths.get(wbLoop));
            excelNames = new String[] {
                    Constants.EXCEL_NAME_MASTER,
                    Constants.EXCEL_NAME_CAR,
                    Constants.EXCEL_NAME_GERARD,
                    Constants.EXCEL_NAME_RON,
            };

            excelPaths = new String[] {
                    Constants.EXCEL_PATH,
                    Constants.EXCEL_CAR,
                    Constants.EXCEL_GERARD,
                    Constants.EXCEL_RON,
            };
        }
    }
}
