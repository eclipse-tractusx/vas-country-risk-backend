package com.catenax.valueaddedservice.service.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVFileReader {


    ArrayList<String> TutorialT = new ArrayList<>();
    public static String TYPE = "text/csv";
    static String[] HEADERS = {"id","country", "score"};

    //Finds if it is an CSV File
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    //Gets CSV Data
    public static List<DataSourceValue> getCsvData(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,  CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader())) {

            List<DataSourceValue> dataSourceValues = new ArrayList<DataSourceValue>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                DataSourceValue dataSourceValue = new DataSourceValue(
                        csvRecord.get("country"),
                        Float.parseFloat(csvRecord.get("score")));
                dataSourceValues.add(dataSourceValue);
            }
            return dataSourceValues;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }
}
