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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.RangeType;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.RangeDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RangeRepository;
import org.eclipse.tractusx.valueaddedservice.service.csv.ResponseMessage;
import org.eclipse.tractusx.valueaddedservice.service.logic.RangeLogicService;
import org.eclipse.tractusx.valueaddedservice.utils.PostgreSQLContextInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
@ContextConfiguration(initializers = PostgreSQLContextInitializer.class)
class RangeIntegartionTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RangeRepository rangeRepository;

    @Autowired
    private RangeLogicService rangeLogicService;

    @AfterEach
    public void cleanUploadReports(){
        rangeRepository.deleteAll();
        rangeLogicService.invalidateAllCache();
    }

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");
        map.put("ratingName", "testRating123");

        return map;
    }

    @Test
    void saveUserRanges() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<RangeDTO> rangeDTOList = createRanges();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveUserRanges?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        UriTemplate uritemplateGet = new UriTemplate("/api/dashboard/getUserRanges?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uritemplateGet.expand(map);
        RequestEntity<Void> requestGet = RequestEntity
                .get(uriGet).build();
        ResponseEntity<List<RangeDTO>> responseEntityGet = testRestTemplate.exchange(requestGet, new ParameterizedTypeReference<>() {});
        List<RangeDTO> list = responseEntityGet.getBody();

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());
        assertNotEquals(0,list.size());

        assertEquals(rangeDTOList.get(0).getValue(), list.get(0).getValue());
        assertEquals(rangeDTOList.get(1).getValue(), list.get(1).getValue());
        assertEquals(rangeDTOList.get(1).getValue(), list.get(1).getValue());
    }

    @Test
    void getUserRanges() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getUserRanges?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<List<RangeDTO>> responseEntity = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});
        List<RangeDTO> list = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());

        assertEquals(VasConstants.MIN_DEFAULT_USER_RANGE, list.get(0).getValue());
        assertEquals(VasConstants.BETWEEN_DEFAULT_USER_RANGE, list.get(1).getValue());
        assertEquals(VasConstants.MAX_DEFAULT_USER_RANGE, list.get(2).getValue());

    }


    @Test
    void updateRanges() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<RangeDTO> rangeDTOList = createRanges();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveUserRanges?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // Change VALUES
        Integer newValue = 20;
        rangeDTOList.get(0).setValue(newValue);

        requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);
        responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        UriTemplate uritemplateGet = new UriTemplate("/api/dashboard/getUserRanges?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uritemplateGet.expand(map);
        RequestEntity<Void> requestGet = RequestEntity
                .get(uriGet).build();
        ResponseEntity<List<RangeDTO>> responseEntityGet = testRestTemplate.exchange(requestGet, new ParameterizedTypeReference<>() {});
        List<RangeDTO> list = responseEntityGet.getBody();

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());
        assertNotEquals(0,list.size());
        assertEquals(list.get(0).getValue(),rangeDTOList.get(0).getValue());
    }

    private RangeDTO createRangeDTO(RangeType rangeType,Integer value,String description){
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setRange(rangeType);
        rangeDTO.setValue(value);
        rangeDTO.setDescription(description);
        rangeDTO.setCompanyUser(createCompanyUserDTO());
        return rangeDTO;
    }

    private CompanyUserDTO createCompanyUserDTO(){

        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        return companyUserDTO;
    }

    private List<RangeDTO> createRanges(){
        RangeDTO minRange = createRangeDTO(RangeType.Min,40,"MIN");
        RangeDTO betweenRange = createRangeDTO(RangeType.Between,75 ,"BetWeen Value");
        RangeDTO maxRANGE = createRangeDTO(RangeType.Max,100,"Max");
        List<RangeDTO> list = new ArrayList<>();
        list.add(minRange);
        list.add(betweenRange);
        list.add(maxRANGE);
        return list;
    }
}


