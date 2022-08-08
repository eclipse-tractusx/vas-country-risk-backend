package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.service.DashboardService;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import com.catenax.valueaddedservice.service.RangeService;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.catenax.valueaddedservice.service.csv.CSVFileReader;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);


    @Autowired
    DashboardService dashboardService;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    RangeService rangeService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/dashboard/getTableInfo")
    public ResponseEntity<List<DashBoardTableDTO>> getAllDashBoardTable(@RequestParam Map<String,Object> ratings,
                                                                   @RequestParam(value = "year",defaultValue = "0",required = false) Integer year ,
                                                                   CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardTableDTO> dashBoardTableDTOs;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if(ratings.get("ratings") != null && !String.valueOf(ratings.get("ratings")).isEmpty() ){
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get("ratings")), new TypeReference<>() {});
        }

        dashBoardTableDTOs = dashboardService.getTableInfo(year, ratingDTOS,companyUser);
        return ResponseEntity.ok().body(dashBoardTableDTOs);
    }

    @GetMapping("/dashboard/getWorldMap")
    public ResponseEntity<List<DashBoardWorldMapDTO>> getDashBoardWorldMap(@RequestParam Map<String,Object> ratings,
                                                                        @RequestParam(value = "year",defaultValue = "0",required = false) Integer year ,
                                                                        CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if(ratings.get("ratings") != null && !String.valueOf(ratings.get("ratings")).isEmpty() ){
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get("ratings")), new TypeReference<>() {});
        }

        dashBoardWorldMapDTOS = dashboardService.getWorldMapInfo(year, ratingDTOS,companyUser);
        return ResponseEntity.ok().body(dashBoardWorldMapDTOS);
    }

    //API to get Ratings by Year
    @GetMapping("/dashboard/allYears")
    public ResponseEntity<List<Integer>>getYears(){
        List<Integer> years;
        years = dataSourceService.findAllYears();
        return ResponseEntity.ok().body(years);
    }

    //API to get All Years
    @GetMapping("/dashboard/ratingsByYear")
    public ResponseEntity<List<DataSourceDTO>> ratingsByYear (@RequestParam(value = "year",defaultValue = "0",required = false) Integer year) {
        List<DataSourceDTO> dataSourceDto;
        dataSourceDto = dataSourceService.findRatingsByYear(year);
        return ResponseEntity.ok().body(dataSourceDto);
    }

    @GetMapping("/dashboard/getTemplate")
    public ResponseEntity<byte[]> getTemplate () {
        FileDTO fileDTO  = dashboardService.getDataSourceTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + fileDTO.getFileName()+".csv");
        httpHeaders.set("filename",fileDTO.getFileName()+".csv");
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileDTO.getContent().getBytes());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(byteArrayResource.getByteArray());

    }

    @PostMapping("/dashboard/readCSV")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("name") String Filename,
                                                      @RequestParam("ratingname") String DataSourceName) {
        String message = "";
        if (CSVFileReader.hasCSVFormat(file)) {
            try {
                dashboardService.saveCsv(file,Filename,DataSourceName);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    //API to get Current User Ranges
    @GetMapping("/dashboard/UserRanges")
    public ResponseEntity<List<Integer>> userRanges () {
        List<RangeDTO> RangeDto;
        //RangeDto = rangeService.getAllRanges(null);
        List<Integer> values;
        values = rangeService.getAllRanges(null);
        Collections.sort(values);
        return ResponseEntity.ok().body(values);
    }

    @PostMapping("/dashboard/sendRanges")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("rangeHigh") Integer rangeHigh, @RequestParam("rangeMid") Integer rangeMid
    ,@RequestParam("rangeLow") Integer rangeLow) {
        String message = "";

        try {
            dashboardService.saveRanges(rangeHigh,rangeMid,rangeLow);
            message = "Range successfully saved!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not save the ranges values!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

}
