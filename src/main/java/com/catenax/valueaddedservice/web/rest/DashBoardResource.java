package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.service.CountryService;
import com.catenax.valueaddedservice.service.DashboardService;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.RangeService;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
    CountryService countryService;

    @Autowired
    RangeService rangeService;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/dashboard/getTableInfo")
    public ResponseEntity<List<DashBoardTableDTO>> getAllDashBoardTable(@RequestParam Map<String, Object> ratings,
                                                                        @RequestParam(value = "year", defaultValue = "0", required = false) Integer year,
                                                                        CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardTableDTO> dashBoardTableDTOs;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if (ratings.get("ratings") != null && !String.valueOf(ratings.get("ratings")).isEmpty()) {
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get("ratings")), new TypeReference<>() {
            });
        }

        dashBoardTableDTOs = dashboardService.getTableInfo(year, ratingDTOS, companyUser);
        return ResponseEntity.ok().body(dashBoardTableDTOs);
    }

    @GetMapping("/dashboard/getWorldMap")
    public ResponseEntity<List<DashBoardWorldMapDTO>> getDashBoardWorldMap(@RequestParam Map<String, Object> ratings,
                                                                           @RequestParam(value = "year", defaultValue = "0", required = false) Integer year,
                                                                           CompanyUserDTO companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if (ratings.get("ratings") != null && !String.valueOf(ratings.get("ratings")).isEmpty()) {
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get("ratings")), new TypeReference<>() {
            });
        }

        dashBoardWorldMapDTOS = dashboardService.getWorldMapInfo(year, ratingDTOS, companyUser);
        return ResponseEntity.ok().body(dashBoardWorldMapDTOS);
    }

    //API to get Ratings by Year
    @GetMapping("/dashboard/allYears")
    public ResponseEntity<List<Integer>> getYears() {
        List<Integer> years;
        years = dataSourceService.findAllYears();
        return ResponseEntity.ok().body(years);
    }

    //API to get All Years
    @GetMapping("/dashboard/ratingsByYear")
    public ResponseEntity<List<DataSourceDTO>> ratingsByYear(@RequestParam(value = "year", defaultValue = "0", required = false) Integer year) {
        List<DataSourceDTO> dataSourceDto;
        dataSourceDto = dataSourceService.findRatingsByYear(year);
        return ResponseEntity.ok().body(dataSourceDto);
    }

    @GetMapping("/dashboard/getTemplate")
    public ResponseEntity<byte[]> getTemplate() {
        FileDTO fileDTO = dashboardService.getDataSourceTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDTO.getFileName() + ".csv");
        httpHeaders.set("filename", fileDTO.getFileName() + ".csv");
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayResource byteArrayResource = new ByteArrayResource(fileDTO.getContent().getBytes());
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(byteArrayResource.getByteArray());

    }

    @PostMapping("/dashboard/uploadCsv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,
                                                      @RequestHeader("ratingName") String dataSourceName, CompanyUserDTO companyUser) {
        String message = "";
        // TO DO Remove hardcoded User

        companyUser.setName("test user");
        companyUser.setCompany("test");
        companyUser.setEmail("test_user@mail.com");
        companyUser.setId(1L);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        try {
            dashboardService.saveCsv(file, dataSourceName, companyUser);
        } catch (DataIntegrityViolationException e) {
            message = "Could not upload the file duplicate name: " + dataSourceName + "!";
            log.error(message);
            log.error("Error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage( e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));


    }

    //API to get Current User Ranges
    @GetMapping("/dashboard/getUserRanges")
    public ResponseEntity<List<RangeDTO>> userRanges(CompanyUserDTO companyUser) {

        // TO DO Remove hardcoded User

        companyUser.setName("test user");
        companyUser.setCompany("test");
        companyUser.setEmail("test_user@mail.com");
        companyUser.setId(1L);
        List<RangeDTO> rangeDTOS;
        rangeDTOS = rangeService.getUserRangesOrDefault(companyUser);
        return ResponseEntity.ok().body(rangeDTOS);
    }

    @GetMapping("/dashboard/getCountrys")
    public ResponseEntity<List<CountryDTO>> getCountrys() {
        return ResponseEntity.ok().body(countryService.findAll());
    }

    @PostMapping("/dashboard/saveUserRanges")
    public ResponseEntity<ResponseMessage> saveRanges(@Valid @RequestBody List<RangeDTO> rangeDTOS, CompanyUserDTO companyUserDTO) {
        String message = "";

        // TO DO Remove hardcoded User
        companyUserDTO.setName("test user");
        companyUserDTO.setCompany("test");
        companyUserDTO.setEmail("test_user@mail.com");
        companyUserDTO.setId(1L);
        rangeDTOS.forEach(rangeDTO -> rangeDTO.setCompanyUser(companyUserDTO));

        try {
            dashboardService.saveRanges(rangeDTOS, companyUserDTO);
            message = "Range successfully saved!";
            log.info(message);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception e) {
            message = "Could not save the ranges values!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ResponseMessage(message));
        }
    }

}
