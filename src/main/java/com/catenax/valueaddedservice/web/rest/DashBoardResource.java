package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.DashBoardDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @Value("${application.host}")
    private String host;

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashBoardDto>> getAllDashBoard(Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardDto> dashBoardDtos = new ObjectMapper().readValue("[{\"id\":1,\"bpn\":\"mercedes123\",\"legalName\":\"mercedes\",\"address\":\"berlim\",\"city\":null,\"country\":\"DE\",\"score\":76.0,\"rating\":\"CPI rating\"},{\"id\":2,\"bpn\":\"bmw123\",\"legalName\":\"bmw\",\"address\":\"berlim\",\"city\":null,\"country\":\"DE\",\"score\":90.0,\"rating\":\"CPI rating\"},{\"id\":3,\"bpn\":\"bosh123\",\"legalName\":\"bosh\",\"address\":\"berlim\",\"city\":null,\"country\":\"FR\",\"score\":50.0,\"rating\":\"Basel rating\"},{\"id\":4,\"bpn\":\"custom123\",\"legalName\":\"custom\",\"address\":\"lisbon\",\"city\":null,\"country\":\"PT\",\"score\":10.0,\"rating\":\"Custom rating\"}]"
                ,new TypeReference<List<DashBoardDto>>() {});
//        DashBoardDto dashBoardDto;
//        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/config/fake-data/dashboard.csv"));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            String[] field = line.split(";");
//            dashBoardDto = new DashBoardDto();
//            dashBoardDto.setId(Long.valueOf(field[0]));
//            dashBoardDto.setBpn(field[1]);
//            dashBoardDto.setLegalName(field[2]);
//            dashBoardDto.setAddress(field[3]);
//            dashBoardDto.setCountry(field[4]);
//            dashBoardDto.setScore(Float.valueOf(field[5]));
//            dashBoardDto.setRating(field[6]);
//            dashBoardDtos.add(dashBoardDto);
//        }
        return ResponseEntity.ok().body(dashBoardDtos);
    }


    @Scheduled(cron = "0 * * * * *")
    public ResponseEntity<List<DashBoardDto>> makeSchedule() throws IOException {
        log.info("REST request to make a schedule {} ",host);
        var headers = new HttpHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> rest = restTemplate.exchange(host, HttpMethod.GET,request,Object.class);
        log.info(rest.getBody().toString());
        return ResponseEntity.ok().body(new ArrayList<>());
    }

}
