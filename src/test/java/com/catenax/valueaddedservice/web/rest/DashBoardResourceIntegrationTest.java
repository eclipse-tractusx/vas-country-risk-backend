package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.dto.DashBoardWorldMapDTO;
import com.catenax.valueaddedservice.dto.RatingDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriTemplate;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
@ActiveProfiles(profiles = "test")
class DashBoardResourceIntegrationTest {


    @Value(value = "classpath:config/liquibase/test-data/ListRating.json")
    private Resource listRatingJson;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @Transactional
    void getTableInfo() throws Exception {


        List<RatingDTO> ratingDTOS = objectMapper.readValue(listRatingJson.getInputStream(), new TypeReference<List<RatingDTO>>() {
        });

        Map<String,Object> map = new HashMap<>();
        map.put("year",2021);
        map.put("ratings",objectMapper.writeValueAsString(ratingDTOS));
        map.put("company","Test Company");
        map.put("name","John");
        map.put("email","john@testcompany.com");
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getWorldMap?year={year}&ratings={ratings}&name={name}&company={company}&email={email}");

        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<DashBoardWorldMapDTO> list = (List<DashBoardWorldMapDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(list.isEmpty(),list.size());



    }

}

