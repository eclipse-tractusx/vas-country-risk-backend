package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.DashBoardDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashBoardDTO>> getAllDashBoard(Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardDTO> dashBoardDtos = new ObjectMapper().readValue(json.getInputStream(),new TypeReference<List<DashBoardDTO>>() {});

        return ResponseEntity.ok().body(dashBoardDtos);
    }


}
