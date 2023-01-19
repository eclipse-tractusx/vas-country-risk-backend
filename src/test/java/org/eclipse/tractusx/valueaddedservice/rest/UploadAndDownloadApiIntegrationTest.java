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
import org.eclipse.tractusx.valueaddedservice.domain.DataSourceValue;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.repository.DataSourceRepository;
import org.eclipse.tractusx.valueaddedservice.repository.DataSourceValueRepository;
import org.eclipse.tractusx.valueaddedservice.service.csv.ResponseMessage;
import org.eclipse.tractusx.valueaddedservice.service.mapper.DataSourceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
class UploadAndDownloadApiIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private DataSourceValueRepository dataSourceValueRepository;

    @Autowired
    private DataSourceMapper dataSourceMapper;


    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        return map;
    }


    @Test
    void uploadCsv() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", VasConstants.HEADER_CSV_NAME);
        headers.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        headers.set("type", VasConstants.CSV_ROLE_TYPE_GLOBAL);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder(VasConstants.CSV_TYPE)
                .name(VasConstants.CSV_NAME)
                .filename(VasConstants.CSV_FILENAME)
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH)), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(VasConstants.CSV_NAME, fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Get Current Year
        map.put("year", Calendar.getInstance().get(Calendar.YEAR));

        //Gets Ratings By Year
        UriTemplate uritemplateRatings = new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&companyName={companyName}&email={email}");
        URI uriRatings = uritemplateRatings.expand(map);
        RequestEntity<Void> requestRatings = RequestEntity
                .get(uriRatings).build();
        ResponseEntity<List<DataSourceDTO>> responseRatings = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = responseRatings.getBody();

        assertNotEquals(0,dataSourceDTOList.size());

        DataSourceDTO dataSourceDTO = dataSourceDTOList.get(0);
        assertEquals(VasConstants.HEADER_CSV_NAME, dataSourceDTO.getFileName());


        cleanDataSources(dataSourceDTO);
    }

    @Test
    @Transactional
    void getDataSourceTemplate() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTemplate?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).accept(MediaType.APPLICATION_OCTET_STREAM).build();
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(request,String.class);
        String data =  responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,data.length());

    }

    @Test
    void errorOnUpload() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", VasConstants.HEADER_CSV_NAME_ERROR);
        headers.setBearerAuth(VasConstants.HEADER_FAKE_TOKEN);
        headers.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        headers.set("type", VasConstants.CSV_ROLE_TYPE_COMPANY);


        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder(VasConstants.CSV_TYPE)
                .name(VasConstants.CSV_NAME)
                .filename(VasConstants.CSV_FILENAME)
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH)), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(VasConstants.CSV_NAME, fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Get Current Year
        map.put("year", Calendar.getInstance().get(Calendar.YEAR));

        //Gets Ratings By Year
        UriTemplate uritemplateRatings = new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&companyName={companyName}&email={email}");
        URI uriRatings = uritemplateRatings.expand(map);
        RequestEntity<Void> requestRatings = RequestEntity
                .get(uriRatings).build();
        ResponseEntity<List<DataSourceDTO>> responseRatings = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = responseRatings.getBody();

        assertNotEquals(0,dataSourceDTOList.size());

        DataSourceDTO dataSourceDTO = dataSourceDTOList.get(0);
        assertEquals(VasConstants.HEADER_CSV_NAME_ERROR, dataSourceDTO.getFileName());


        responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());

        cleanDataSources(dataSourceDTO);
    }

    @Test
    void uploadCsvAndDeleteCSV() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", VasConstants.HEADER_CSV_NAME);
        headers.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        headers.set("type", VasConstants.CSV_ROLE_TYPE_GLOBAL);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder(VasConstants.CSV_TYPE)
                .name(VasConstants.CSV_NAME)
                .filename(VasConstants.CSV_FILENAME)
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH)), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(VasConstants.CSV_NAME, fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Get Current Year
        map.put("year", Calendar.getInstance().get(Calendar.YEAR));

        //Gets Ratings By Year
        UriTemplate uriTemplateRatings = new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&companyName={companyName}&email={email}");
        URI uriRatings = uriTemplateRatings.expand(map);
        RequestEntity<Void> requestRatings = RequestEntity
                .get(uriRatings).build();
        ResponseEntity<List<DataSourceDTO>> responseRatings = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = responseRatings.getBody();

        assertNotEquals(0,dataSourceDTOList.size());

        DataSourceDTO dataSourceDTO = dataSourceDTOList.get(0);
        assertEquals(VasConstants.HEADER_CSV_NAME, dataSourceDTO.getFileName());

        uriTemplateRatings = new UriTemplate("/api/dashboard/deleteRating/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",dataSourceDTO.getId());
        uriRatings = uriTemplateRatings.expand(map);
        requestRatings = RequestEntity
                .delete(uriRatings).build();

        int status = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {}).getStatusCode().value();
        assertEquals(HttpStatus.NO_CONTENT.value(),status);


    }

    @Test
    void uploadCsvAndDeleteCSVFromOtherUserError() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", VasConstants.HEADER_CSV_NAME);
        headers.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        headers.set("type", VasConstants.CSV_ROLE_TYPE_CUSTOM);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder(VasConstants.CSV_TYPE)
                .name(VasConstants.CSV_NAME)
                .filename(VasConstants.CSV_FILENAME)
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH)), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(VasConstants.CSV_NAME, fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Get Current Year
        map.put("year", Calendar.getInstance().get(Calendar.YEAR));

        //Gets Ratings By Year
        UriTemplate uriTemplateRatings = new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&companyName={companyName}&email={email}");
        URI uriRatings = uriTemplateRatings.expand(map);
        RequestEntity<Void> requestRatings = RequestEntity
                .get(uriRatings).build();
        ResponseEntity<List<DataSourceDTO>> responseRatings = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {});
        List<DataSourceDTO> dataSourceDTOList = responseRatings.getBody();

        assertNotEquals(0,dataSourceDTOList.size());

        DataSourceDTO dataSourceDTO = dataSourceDTOList.get(0);
        assertEquals(VasConstants.HEADER_CSV_NAME, dataSourceDTO.getFileName());

        uriTemplateRatings = new UriTemplate("/api/dashboard/deleteRating/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",dataSourceDTO.getId());
        map.put("name","Not john");
        uriRatings = uriTemplateRatings.expand(map);
        requestRatings = RequestEntity
                .delete(uriRatings).build();

        int status = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {}).getStatusCode().value();
        assertEquals(HttpStatus.UNAUTHORIZED.value(),status);

        cleanDataSources(dataSourceDTO);
    }

    @Test
    void uploadCsvAndDeleteCSVNotFoundError() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uriTemplateRatings = new UriTemplate("/api/dashboard/deleteRating/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",Integer.MAX_VALUE);

        URI uriRatings = uriTemplateRatings.expand(map);
        RequestEntity requestRatings = RequestEntity
                .delete(uriRatings).build();

        int status = testRestTemplate.exchange(requestRatings, new ParameterizedTypeReference<>() {}).getStatusCode().value();
        assertEquals(HttpStatus.NOT_FOUND.value(),status);

    }

    @Test
    void uploadCsvWithErrorOnScore() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", VasConstants.HEADER_CSV_NAME);
        headers.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        headers.set("type", VasConstants.CSV_ROLE_TYPE_GLOBAL);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder(VasConstants.CSV_TYPE)
                .name(VasConstants.CSV_NAME)
                .filename(VasConstants.CSV_FILENAME)
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH_ERROR)), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(VasConstants.CSV_NAME, fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseEntity.getStatusCode());


    }

    private void cleanDataSources(DataSourceDTO dataSourceDTO) {


        List<DataSourceValue> deleteList = dataSourceValueRepository.findByDataSource(dataSourceMapper.toEntity(dataSourceDTO));

        assertNotEquals(0,deleteList.size());

        dataSourceValueRepository.deleteAll(deleteList);

        dataSourceRepository.deleteById(dataSourceDTO.getId());
    }


}


