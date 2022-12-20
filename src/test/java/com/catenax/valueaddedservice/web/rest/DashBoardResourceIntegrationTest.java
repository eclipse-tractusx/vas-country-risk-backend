package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
import com.catenax.valueaddedservice.dto.DashBoardWorldMapDTO;
import com.catenax.valueaddedservice.dto.RatingDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
class DashBoardResourceIntegrationTest {


    @Value(value = "classpath:config/liquibase/test-data/ListRating.json")
    private Resource listRatingJson;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ObjectMapper objectMapper;


    private Map<String,Object> getMap() throws IOException {
       RatingDTO ratingDTO = objectMapper.readValue(listRatingJson.getInputStream(), RatingDTO.class);

        Map<String,Object> map = new HashMap<>();
        map.put("year",2021);
        map.put("ratings",objectMapper.writeValueAsString(ratingDTO));
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        map.put("ratingName", "testRating123");

        return map;
    }



    @Test
    @Transactional
    void getTableInfo() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTableInfo?year={year}&ratings[]={ratings}&name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<DashBoardTableDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<DashBoardTableDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getWorldMapInfo() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getWorldMap?year={year}&ratings[]={ratings}&name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<List<DashBoardWorldMapDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        List<DashBoardWorldMapDTO> list =  responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

}


