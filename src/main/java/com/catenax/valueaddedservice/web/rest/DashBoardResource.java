package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.DashBoardDTO;
import com.catenax.valueaddedservice.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    @Autowired
    DashboardService dashboardService;


    @GetMapping("/dashboard/getTableInfo")
    public ResponseEntity<List<DashBoardDTO>> getAllDashBoard(Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardDTO> dashBoardDtos = new ArrayList<>();
        dashBoardDtos = dashboardService.getTableInfo();
        return ResponseEntity.ok().body(dashBoardDtos);
    }


}
