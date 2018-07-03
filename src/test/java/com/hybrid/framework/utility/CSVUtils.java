package com.hybrid.framework.utility;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.testng.Reporter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    private String line = "";
    private final String SPLIT_BY = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; // This regex expression covers not only the separation of comma, but when there is a comma inside a 'value', it ignores it and doesn't include in separation.
    private int lastRow;
    private static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\test\\java\\com\\b2b\\automation\\dataEngine\\";
    private static final String FILE_FORMAT = ".csv";
    private static String[] data;
    private int commaLength;
    private CsvParserSettings settings = new CsvParserSettings();
    private CsvParser parser;

    public CSVUtils() throws IOException {}

    private List<String[]> parseCsvUsingDependency(String fileName) {
        settings.getFormat().setLineSeparator("\n");
        parser = new CsvParser(settings);
        return parser.parseAll(new File(FILE_PATH + fileName + FILE_FORMAT));
    }

    public String data(String fileName, int rowIndex, int columnIndex) {

        String csvFile = FILE_PATH + fileName + FILE_FORMAT;
        List<String> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Use regex as separator - for this experiment, only use this array when outputting a specific column.
                data = line.split(SPLIT_BY);
                if (data[columnIndex].length() > 0) {
                    dataList.add(data[columnIndex]);
                }
                else {
                    break;
                }
            }
            return dataList.get(rowIndex);
        }
        catch (Exception e) {
            return dataList.get(rowIndex);
        }
    }

    private int getCommaLength(String fileName) {
        String csvFile = FILE_PATH + fileName + FILE_FORMAT;
        commaLength = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Use regex as separator - for this experiment, only use this array when outputting a specific column.
                data = line.split(SPLIT_BY);
                if (data.length > 1) { // Get the number of columns through identifying the header length.
                    commaLength = data.length;
                }
            }
            return commaLength;
        }
        catch (Exception e) {
            return 1;
        }
    }

    public String getCsvValue(String fileName, int rowIndex, int columnIndex) {
        String extractedData;
        try {
            extractedData = this.data(fileName, rowIndex, columnIndex);
            // We remove all unnecessary double quotes from the parsed data.
            if (extractedData.contains("\"")) {
                extractedData = extractedData.replace("\"", "");
            }
            return extractedData;
        } catch (NullPointerException e) {
            return "";
        }
    }

    public int getLastRowIndex(String fileName) {
        String csvFile = FILE_PATH + fileName + FILE_FORMAT;
        lastRow = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                this.lastRow++;
            }
            return this.lastRow - 1; // Subtract by 1 to get the index number of last value.
        }
        catch (Exception e) {
            return 1;
        }
    }

    public void writeValue(String newValue, String fileName, int columnIndex) throws IOException {
        try {
            String delimiter = ",";
            FileWriter fileWriter = new FileWriter(FILE_PATH + fileName + FILE_FORMAT, true);
            fileWriter.append(newValue);
            // Append comma depending on the HEADER amount minus 1.
            for (int i = 0; i < getCommaLength(fileName) - 1; i++) {
                fileWriter.append(delimiter);
            }
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e) {
            Reporter.log("Failed to write the new value '" + newValue + "' to " + fileName + ".");
            e.printStackTrace();
        }
    }

    public void replaceValue(String newValue, String fileName, int rowIndex, int columnIndex) throws IOException {
        String delimiter = ",";
        String csvFile = FILE_PATH + fileName + FILE_FORMAT;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] data;
            List<String[]> csvBody = new ArrayList<String[]>();
            while ((line = br.readLine()) != null) {
                data = line.split(SPLIT_BY);
                csvBody.add(data);
            }
            csvBody.get(rowIndex)[columnIndex] = newValue;
            br.close();

            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(csvFile));
            for (int i = 0; i < csvBody.size(); i++) {
                for(String value : csvBody.get(i)){
                    outputWriter.write(value);
                    if(!value.equals(csvBody.get(i)[csvBody.get(i).length-1]))
                        outputWriter.write(delimiter);
                }
                if(i!=csvBody.size()) {
                    outputWriter.newLine();
                }
            }
            outputWriter.flush();
            outputWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
