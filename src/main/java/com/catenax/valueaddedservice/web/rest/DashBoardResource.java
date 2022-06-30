package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.DashBoardDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashBoardDto>> getAllDashBoard(Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardDto> dashBoardDtos = new ArrayList<>();
        DashBoardDto dashBoardDto;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/config/fake-data/dashboard.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] field = line.split(";");
            dashBoardDto = new DashBoardDto();
            dashBoardDto.setId(Long.valueOf(field[0]));
            dashBoardDto.setBpn(field[1]);
            dashBoardDto.setLegalName(field[2]);
            dashBoardDto.setAddress(field[3]);
            dashBoardDto.setCountry(field[4]);
            dashBoardDto.setScore(Float.valueOf(field[5]));
            dashBoardDto.setRating(field[6]);
            dashBoardDtos.add(dashBoardDto);
        }
        return ResponseEntity.ok().body(dashBoardDtos);
    }

}
