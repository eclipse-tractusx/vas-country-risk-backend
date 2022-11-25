package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareBusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareDataSourceDTO;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareDTO;
import com.catenax.valueaddedservice.repository.DataSourceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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
class ShareIntegrationTest {

    @Value(value = "classpath:config/liquibase/test-data/sharedatasource.json")
    private Resource listDataSource;

    @Value(value = "classpath:config/liquibase/test-data/bpns.json")
    private Resource listBpn;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    DataSourceRepository dataSourceRepository;

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("year",2021);
        map.put("company", "test");

        return map;
    }

    private Map<String,Object> getMapLists() throws IOException {
        Map<String,Object> map = new HashMap<>();

        List<DataSourceDTO> dataSourceDTOS = objectMapper.readValue(listDataSource.getInputStream(), new TypeReference<List<DataSourceDTO>>() { });

        List<BusinessPartnerDTO> businessPartnerDTOS = objectMapper.readValue(listBpn.getInputStream(), new TypeReference<List<BusinessPartnerDTO>>() { });

        map.put("ratings",objectMapper.writeValueAsString(dataSourceDTOS));
        map.put("bpns",objectMapper.writeValueAsString(businessPartnerDTOS));
        map.put("company", "test");

        return map;
    }

    @AfterEach
    public void cleanReports(){
        //dataSourceRepository.deleteAll();
    }


    @Test
    void getRatingsForShareAPI () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/shareRatings?company={company}&year={year}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, uri);

        ResponseEntity<List<DataSourceDTO>> response = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,dataSourceDTOList.size());

    }

    @Test
    void getMappedRatingsForShareAPI () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = getMapLists();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/shareMappedRatings?company={company}&ratings={ratings}&bpns={bpns}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, uri);

        ResponseEntity<List<ShareDTO>> response = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        List<ShareDTO> shareDTOSList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,shareDTOSList.size());

    }

}


