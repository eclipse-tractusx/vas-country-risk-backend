/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.dto.AuthPropertiesDTO;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.ShareDTO;
import org.eclipse.tractusx.valueaddedservice.utils.MockUtilsTest;
import org.eclipse.tractusx.valueaddedservice.utils.PostgreSQLContextInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ValueAddedServiceApplication.class)
@ContextConfiguration(initializers = PostgreSQLContextInitializer.class)
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

    @MockBean
    ApplicationVariables applicationVariables;

    MockUtilsTest mockUtilsTest;

    @BeforeAll
    public void beforeAll() {
        mockUtilsTest = new MockUtilsTest();
        mockUtilsTest.beforeAll();
    }


    @BeforeEach
    public void setUp() throws JsonProcessingException, InterruptedException {
        mockUtilsTest.openPorts();
        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("TestCompany");
        authPropertiesDTO.setEmail("test@email.com");
        authPropertiesDTO.setName("TestName");

        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"Cl16-CX-CRisk\": { \"roles\": [ \"User\", \"Company Admin\", \"read_suppliers\", \"read_customers\" ] } }";
        Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        authPropertiesDTO.setResourceAccess(map);

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);

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
        UriTemplate uritemplate= new UriTemplate("/api/getAllRatingsForCompany?companyName={companyName}&year={year}&name={name}&companyName={companyName}&email={email}\"");
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
        map.putAll(getMap());
        UriTemplate uritemplate= new UriTemplate("/api/getAllRatingsScoresForEachBpn?companyName={companyName}&datasource[]={datasource}&bpns[]={bpns}&name={name}&companyName={companyName}&email={email}\"");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(headers, HttpMethod.GET, uri);

        ResponseEntity<List<ShareDTO>> response = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
        List<ShareDTO> shareDTOSList = response.getBody();

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotEquals(0,shareDTOSList.size());

    }

    private Map<String,Object> getMapLists() throws IOException {
        Map<String,Object> map = new HashMap<>();

        DataSourceDTO dataSourceDTOS = objectMapper.readValue(listDataSource.getInputStream(), DataSourceDTO.class);

       BusinessPartnerDTO businessPartnerDTOS = objectMapper.readValue(listBpn.getInputStream(), BusinessPartnerDTO.class);

        map.put("datasource",objectMapper.writeValueAsString(dataSourceDTOS));
        map.put("bpns",objectMapper.writeValueAsString(businessPartnerDTOS));
        map.put("companyName", "test");

        return map;
    }


}


