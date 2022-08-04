package com.catenax.valueaddedservice.service.csv;

import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    public static List<DataSourceValueDTO> getCsvData(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,  CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader())) {

            List<DataSourceValueDTO> dataSourceValues = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                DataSourceValueDTO dataSourceValue= new DataSourceValueDTO();
                dataSourceValue.setCountry(csvRecord.get("country"));
                dataSourceValue.setScore(Float.valueOf(csvRecord.get("score")));
                // TODO preencher o resto do objeto com os valores do country - tens de ir chamar o service country e procurar essa country
                dataSourceValues.add(dataSourceValue);
            }
            return dataSourceValues;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }
}
