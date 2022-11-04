package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
class RatingApiIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("company","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");
        return map;
    }

    @Test
    @Transactional
    void allYears() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/allYears?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<Integer>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<Integer> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void ratingsByYear() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/allYears?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<Integer>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<Integer> list = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());

        map.put("year",list.get(0));
        uritemplate= new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&company={company}&email={email}");
        uri = uritemplate.expand(map);
        request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<DataSourceDTO>> response = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,dataSourceDTOList.size());
    }
}


