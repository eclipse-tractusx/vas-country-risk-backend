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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.dto.DashBoardTableDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DashBoardWorldMapDTO;
import org.eclipse.tractusx.valueaddedservice.dto.RatingDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ResponsePageDTO;
import org.eclipse.tractusx.valueaddedservice.utils.PostgreSQLContextInitializer;
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

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
@ContextConfiguration(initializers = PostgreSQLContextInitializer.class)
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
        ResponseEntity<ResponsePageDTO<DashBoardTableDTO>> responseEntity = testRestTemplate.exchange(request,new ParameterizedTypeReference<>() {});
        List<DashBoardTableDTO> list = responseEntity.getBody().getContent();

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

    @Test
    @Transactional
    void getWorldMapInfoWithRatingNonExist() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDataSourceName("NonExist");
        ratingDTO.setWeight(100F);
        Map<String,Object> map = getMap();
        map.put("nonExistRating",objectMapper.writeValueAsString(ratingDTO));
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getWorldMap?year={year}&ratings[]={ratings}&ratings[]={nonExistRating}&name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<List<DashBoardWorldMapDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        List<DashBoardWorldMapDTO> list =  responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getTableInfoInfoWithRatingTwoExistRating() throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setDataSourceName("Economist Intelligence Unit Country Ratings");
        ratingDTO.setWeight(50F);
        Map<String,Object> map = getMap();
        map.put("secondExistRating",objectMapper.writeValueAsString(ratingDTO));
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTableInfo?year={year}&ratings[]={ratings}&ratings[]={secondExistRating}&name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<ResponsePageDTO<DashBoardTableDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {
        });
        List<DashBoardTableDTO> list =  responseEntity.getBody().getContent();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

}


