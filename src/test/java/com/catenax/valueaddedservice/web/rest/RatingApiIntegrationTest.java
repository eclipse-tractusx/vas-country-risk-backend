package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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

    @Autowired
    ObjectMapper objectMapper;

    @Value(value = "classpath:config/liquibase/test-data/sharedatasource.json")
    private Resource listDataSource;

    @Value(value = "classpath:config/liquibase/test-data/bpns.json")
    private Resource listBpn;

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");
        map.put("year",2021);
        return map;
    }

    @Test
    @Transactional
    void allYears() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/allYears?name={name}&companyName={companyName}&email={email}");
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
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/allYears?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<Integer>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<Integer> list = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());

        map.put("year",list.get(0));
        uritemplate= new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&companyName={companyName}&email={email}");
        uri = uritemplate.expand(map);
        request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<DataSourceDTO>> response = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,dataSourceDTOList.size());
    }

    @Test
    void getAllRatingsForCompany () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllRatingsForCompany?companyName={companyName}&year={year}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, uri);

        ResponseEntity<List<DataSourceDTO>> response = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,dataSourceDTOList.size());

    }

    @Test
    void getAllRatingsScoresForEachBpn () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = getMapLists();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getAllRatingsScoresForEachBpn?companyName={companyName}&ratings={ratings}&bpns={bpns}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, uri);

        ResponseEntity<List<ShareDTO>> response = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        List<ShareDTO> shareDTOSList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,shareDTOSList.size());

    }

    private Map<String,Object> getMapLists() throws IOException {
        Map<String,Object> map = new HashMap<>();

        List<DataSourceDTO> dataSourceDTOS = objectMapper.readValue(listDataSource.getInputStream(), new TypeReference<List<DataSourceDTO>>() { });

        List<BusinessPartnerDTO> businessPartnerDTOS = objectMapper.readValue(listBpn.getInputStream(), new TypeReference<List<BusinessPartnerDTO>>() { });

        map.put("ratings",objectMapper.writeValueAsString(dataSourceDTOS));
        map.put("bpns",objectMapper.writeValueAsString(businessPartnerDTOS));
        map.put("companyName", "test");

        return map;
    }


}


