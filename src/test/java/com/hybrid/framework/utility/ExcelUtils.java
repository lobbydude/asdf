package com.hybrid.framework.utility;

import com.hybrid.framework.config.Constants;
import com.hybrid.framework.execution.TestRunner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    private static XSSFSheet excelWSheet;
    private static XSSFWorkbook excelWBook;
    private static XSSFCell cell;
    private static XSSFRow row;
    public static String machineName;
    public static int numberOfWbToSet = 0;
    private static final int totalAmountOfWorkbooks = 4;

    public static void wbLoopAmountSetter() {
        machineName = System.getProperty("user.name");
        switch (machineName.toLowerCase()) {
            case Constants.CAR:
                numberOfWbToSet = 1;
                break;
            case Constants.GERARD:
                numberOfWbToSet = 1;
                break;
            case Constants.RON:
                numberOfWbToSet = 1;
                break;
            case Constants.JAMES:
                numberOfWbToSet = 1;
                break;
            default:
                numberOfWbToSet = totalAmountOfWorkbooks;
                break;
        }
    }

    public static void setExcelFile(String Path) {
        try {
            FileInputStream ExcelFile = new FileInputStream(Path);
            excelWBook = new XSSFWorkbook(ExcelFile);
        } catch (IOException e) {
            Log.error(e.getMessage());
            TestRunner.boolResult = false;
        }
    }

    public static String getCellData(int rowNum, int colNum, String sheetName) {
        try {
            excelWSheet = excelWBook.getSheet(sheetName);
            cell = excelWSheet.getRow(rowNum).getCell(colNum);

            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "no";
            }
            else {
                return cell.getStringCellValue();
            }
        }
        catch (Exception e) {
            TestRunner.boolResult = false;
            return "no";
        }
    }

    public static void deleteColumnContents(int rowNum, int lastRowNUm, int colNum, String sheetName) {
        try {
            excelWSheet = excelWBook.getSheet(sheetName);
            cell = excelWSheet.getRow(rowNum).getCell(colNum);

            if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return;
            }
            else {
                for (int startingRow = rowNum; startingRow <= lastRowNUm; startingRow++) {
                    cell.setCellValue(Cell.CELL_TYPE_BLANK);
                }
            }
        }
        catch(Exception e) {
            TestRunner.boolResult = false;
        }
    }

    public static int getRowCount(String sheetName) {
        int iNumber = 0;
        try {
            excelWSheet = excelWBook.getSheet(sheetName);
            iNumber = excelWSheet.getLastRowNum() + 1;
        }
        catch (Exception e) {
            TestRunner.boolResult = false;
        }
        return iNumber;
    }

    public static int getRowContains(String sTestCaseName, int colNum,String sheetName) {
        int iRowNum=0;
        try {
            int rowCount = ExcelUtils.getRowCount(sheetName);
            for (; iRowNum<rowCount; iRowNum++) {
                if  (ExcelUtils.getCellData(iRowNum,colNum,sheetName).equalsIgnoreCase(sTestCaseName)) {
                    break;
                }
            }
        }
        catch (Exception e) {
            TestRunner.boolResult = false;
        }
        return iRowNum;
    }

    public static int getTestStepsCount(String sheetName, String sTestCaseID, int iTestCaseStart) {
        try {
            for (int i=iTestCaseStart;i<=ExcelUtils.getRowCount(sheetName);i++) {
                if(!sTestCaseID.equals(ExcelUtils.getCellData(i, Constants.COL_TESTCASEID, sheetName))) {
                    int number = i;
                    return number;
                }
            }
            excelWSheet = excelWBook.getSheet(sheetName);
            int number = excelWSheet.getLastRowNum()+1;
            return number;
        }
        catch (Exception e) {
            TestRunner.boolResult = false;
            return 0;
        }
    }

    public static void setCellData(String data,  int rowNum, int colNum, String sheetName, String wbPath) {
        try {
            excelWSheet = excelWBook.getSheet(sheetName);
            row = excelWSheet.getRow(rowNum);
            cell = row.getCell(colNum, row.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                cell = row.createCell(colNum);
                cell.setCellValue(data);
            }
            else {
                cell.setCellValue(data);
            }
            FileOutputStream fileOut = new FileOutputStream(wbPath);
            excelWBook.write(fileOut);
            fileOut.close();
            excelWBook = new XSSFWorkbook(new FileInputStream(wbPath));
        }
        catch(Exception e) {
            TestRunner.boolResult = false;
        }
    }
}
