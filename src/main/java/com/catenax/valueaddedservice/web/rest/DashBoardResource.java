package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.dto.RatingDTO;
import com.catenax.valueaddedservice.service.DashboardService;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ObjectMapper objectMapper;

    @GetMapping("/dashboard/getTableInfo")
    public ResponseEntity<List<DashBoardTableDTO>> getAllDashBoard(@RequestParam Map<String,Object> ratings,
                                                                   @RequestParam(value = "year",defaultValue = "0",required = false) Integer year ,
                                                                   CompanyUser companyUser) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardTableDTO> dashBoardTableDTOs;
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        if(ratings.get("ratings") != null && !String.valueOf(ratings.get("ratings")).isEmpty() ){
            ratingDTOS = objectMapper.readValue(String.valueOf(ratings.get("ratings")), new TypeReference<>() {});
        }

        dashBoardTableDTOs = dashboardService.getTableInfo(year, ratingDTOS,companyUser);
        return ResponseEntity.ok().body(dashBoardTableDTOs);
    }

    //API to get Ratings by Year
    @GetMapping("/dashboard/allyears")
    public ResponseEntity getYears () throws IOException {
        List dataSourceDto;
        dataSourceDto = dataSourceService.findAllYears();
        return ResponseEntity.ok().body(dataSourceDto);
    }

    //API to get All Years
    @GetMapping("/dashboard/ratingsbyyear")
    public ResponseEntity<List<DataSourceDTO>> ratingsbyyear (@RequestParam(value = "year",defaultValue = "0",required = false) Integer year)  throws IOException {
        List<DataSourceDTO> dataSourceDto;
        dataSourceDto = dataSourceService.findRatingsByYear(year);
        return ResponseEntity.ok().body(dataSourceDto);
    }

}
