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
import org.eclipse.tractusx.valueaddedservice.domain.CompanyUser;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.ReportDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ReportValuesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyUserRepository;
import org.eclipse.tractusx.valueaddedservice.repository.ReportRepository;
import org.eclipse.tractusx.valueaddedservice.repository.ReportValuesRepository;
import org.eclipse.tractusx.valueaddedservice.service.csv.ResponseMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
class ReportApiIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ReportValuesRepository reportValuesRepository;

    @Autowired
    CompanyUserRepository companyUserRepository;

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","john@email.com");
        map.put("ratingName", "testRating123");

        return map;
    }

    @AfterEach
    public void cleanReports(){
        reportValuesRepository.deleteAll();
        reportRepository.deleteAll();
        companyUserRepository.deleteAll();

    }

    @Test
    void saveReports () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

    }

    @Test
    void errorOnReport() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //################## Duplicated Name on Report ##############
        RequestEntity requestEntityDupe = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntityDupe = testRestTemplate.exchange(requestEntityDupe,String.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntityDupe.getStatusCode());
    }

    @Test
    void getReportsValueByReport () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplate = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplate.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        reportDTO.setId(reportDTOSize.get(0).getId());

        // ######## ReportsByReport #######
        UriTemplate uriTemplateByReport=
                new UriTemplate("/api/dashboard/getReportsValueByReport?id={id}&" +
                        "reportName={reportName}&" +
                        "companyUserName={companyUserName}&" +
                        "name={name}&" +
                        "companyName={companyName}&" +
                        "email={email}" +
                        "&type={type}");

        Map<String,Object> mapByReport = new HashMap<>();
        mapByReport.put("id",reportDTO.getId());
        mapByReport.put("reportName",reportDTO.getReportName());
        mapByReport.put("companyUserName",reportDTO.getCompanyUserName());
        mapByReport.put("companyName",reportDTO.getCompany());
        mapByReport.put("type",reportDTO.getType());
        mapByReport.putAll(map);

        URI uriByReport = uriTemplateByReport.expand(mapByReport);
        RequestEntity<Void> requestByReport = RequestEntity
                .get(uriByReport).build();
        ResponseEntity<List<ReportValuesDTO>> responseEntityByReport = testRestTemplate.exchange(requestByReport, new ParameterizedTypeReference<>() {});
        List<ReportValuesDTO> listByReport =  responseEntityByReport.getBody();

        assertEquals(HttpStatus.OK,responseEntityByReport.getStatusCode());
        assertNotEquals(0,listByReport.size());
    }

    private ReportDTO createReport(){

        List<Object> list = new ArrayList<>();
        list.add("value");
        List<ReportValuesDTO> reportValuesDTOList1 = new ArrayList<>();
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("TestReportTest");
        reportDTO.setCompanyUserName("John");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("TestCompany");
        reportDTO.setEmail("john@email.com");

        reportValuesDTO.setObjectValue(list);
        reportValuesDTO.setName("ReportValues");
        reportValuesDTOList1.add(reportValuesDTO);
        reportDTO.setReportValuesDTOList(reportValuesDTOList1);
        return reportDTO;
    }

    //Update Report Object
    private ReportDTO createReportUpdate(){

        List<Object> list = new ArrayList<>();
        list.add("valueUpdate");
        List<ReportValuesDTO> reportValuesDTOList1 = new ArrayList<>();
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("TestReportTest");
        reportDTO.setCompanyUserName("John");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("TestCompany");
        reportDTO.setEmail("john@email.com");

        reportValuesDTO.setObjectValue(list);
        reportValuesDTO.setName("ReportValuesUpdated");
        reportValuesDTOList1.add(reportValuesDTO);
        reportDTO.setReportValuesDTOList(reportValuesDTOList1);
        return reportDTO;
    }

    @Test
    void saveReportsAndDelete () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());


        // ############# Delete API ##############
        UriTemplate uriTemplateDelete = new UriTemplate("/api/dashboard/deleteReport/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",reportDTOSize.get(0).getId());
        URI uriDelete = uriTemplateDelete.expand(map);


        RequestEntity request = new RequestEntity(HttpMethod.DELETE, uriDelete);

        ResponseEntity responseEntityDelete = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NO_CONTENT,responseEntityDelete.getStatusCode());



    }

    @Test
    void saveReportsAndUpdate () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        // ############# Update API ##############
        //reportValuesDTO name changed from ReportValues to ReportValuesUpdated
        UriTemplate uriTemplateUpdate = new UriTemplate("/api/dashboard/updateReports?name={name}&companyName={companyName}&email={email}");
        URI uriUpdate = uriTemplateUpdate.expand(map);

        ReportDTO reportDTOUpdate = createReportUpdate();

        reportDTOUpdate.setId(reportDTOSize.get(0).getId());

        RequestEntity requestEntityUpdate = new RequestEntity(reportDTOUpdate, headers, HttpMethod.PUT, uriUpdate);

        ResponseEntity<ResponseMessage> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.NO_CONTENT,responseEntityUpdate.getStatusCode());

        // ############# Get Update Report Values ##############
        reportDTO.setId(reportDTOSize.get(0).getId());

        UriTemplate uriTemplateByReport=
                new UriTemplate("/api/dashboard/getReportsValueByReport?id={id}&" +
                        "reportName={reportName}&" +
                        "companyUserName={companyUserName}&" +
                        "name={name}&" +
                        "companyName={companyName}&" +
                        "email={email}" +
                        "&type={type}");

        Map<String,Object> mapByReport = new HashMap<>();
        mapByReport.put("id",reportDTO.getId());
        mapByReport.put("reportName",reportDTO.getReportName());
        mapByReport.put("companyUserName",reportDTO.getCompanyUserName());
        mapByReport.put("companyName",reportDTO.getCompany());
        mapByReport.put("type",reportDTO.getType());
        mapByReport.putAll(map);

        URI uriByReport = uriTemplateByReport.expand(mapByReport);
        RequestEntity<Void> requestByReport = RequestEntity
                .get(uriByReport).build();
        ResponseEntity<List<ReportValuesDTO>> responseEntityByReport = testRestTemplate.exchange(requestByReport, new ParameterizedTypeReference<>() {});
        List<ReportValuesDTO> listByReport =  responseEntityByReport.getBody();

        assertEquals(HttpStatus.OK,responseEntityByReport.getStatusCode());
        assertNotEquals(0,listByReport.size());
        assertEquals("ReportValuesUpdated", listByReport.get(0).getName());
    }

    @Test
    void saveReportsAndDeleteOtherUserError () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());


        // ############# Delete API ##############
        UriTemplate uriTemplateDelete = new UriTemplate("/api/dashboard/deleteReport/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",reportDTOSize.get(0).getId());
        map.put("name","Not John");
        URI uriDelete = uriTemplateDelete.expand(map);


        RequestEntity request = new RequestEntity(HttpMethod.DELETE, uriDelete);

        ResponseEntity responseEntityDelete = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntityDelete.getStatusCode());

    }

    @Test
    void deleteNonExistReportError () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();

        // ############# Delete API ##############
        UriTemplate uriTemplateDelete = new UriTemplate("/api/dashboard/deleteReport/{id}?name={name}&companyName={companyName}&email={email}");
        map.put("id",Integer.MAX_VALUE);
        URI uriDelete = uriTemplateDelete.expand(map);


        RequestEntity request = new RequestEntity(HttpMethod.DELETE, uriDelete);

        ResponseEntity responseEntityDelete = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.NOT_FOUND,responseEntityDelete.getStatusCode());
    }

    @Test
    void saveReportsAndShare () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        // ############# Share the Created Report ##############

        CompanyUser companyUser = new CompanyUser();
        companyUser.setName("Terry");
        companyUser.setCompanyName("TestCompany");
        companyUser.setEmail("Terry@email.com");

        companyUserRepository.save(companyUser);

        ReportDTO reportDTOShare = createReport();

        reportDTOShare.setCompanyUserName("Terry");
        reportDTOShare.setCompany("TestCompany");
        reportDTOShare.setEmail("Terry@email.com");

        UriTemplate uriTemplateShare = new UriTemplate("/api/dashboard/shareReport?name={name}&companyName={companyName}&email={email}");
        URI uriUpdate = uriTemplateShare.expand(map);

        RequestEntity requestEntityUpdate = new RequestEntity(reportDTOShare, headers, HttpMethod.POST, uriUpdate);

        ResponseEntity<ResponseMessage> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.NO_CONTENT,responseEntityUpdate.getStatusCode());

    }


    @Test
    void saveReportsAndShareDuplicatedError () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        // ############# Share the Created Report ##############

        CompanyUser companyUser = new CompanyUser();
        companyUser.setName("Terry");
        companyUser.setCompanyName("TestCompany");
        companyUser.setEmail("Terry@email.com");

        companyUserRepository.save(companyUser);

        ReportDTO reportDTOShare = createReport();

        reportDTOShare.setCompanyUserName("Terry");
        reportDTOShare.setCompany("TestCompany");
        reportDTOShare.setEmail("Terry@email.com");

        UriTemplate uriTemplateShare = new UriTemplate("/api/dashboard/shareReport?name={name}&companyName={companyName}&email={email}");
        URI uriUpdate = uriTemplateShare.expand(map);

        RequestEntity requestEntityUpdate = new RequestEntity(reportDTOShare, headers, HttpMethod.POST, uriUpdate);

        ResponseEntity<ResponseMessage> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.NO_CONTENT,responseEntityUpdate.getStatusCode());

        ResponseEntity<ResponseMessage> responseError = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseError.getStatusCode());

    }

    @Test
    void UpdateNonExistReportError () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = getMap();

        // ############# Update API ##############
        //reportValuesDTO name changed from ReportValues to ReportValuesUpdated
        UriTemplate uriTemplateUpdate = new UriTemplate("/api/dashboard/updateReports?name={name}&companyName={companyName}&email={email}");
        URI uriUpdate = uriTemplateUpdate.expand(map);

        ReportDTO reportDTOUpdate = createReportUpdate();

        reportDTOUpdate.setId(1L);

        RequestEntity requestEntityUpdate = new RequestEntity(reportDTOUpdate, headers, HttpMethod.PUT, uriUpdate);

        ResponseEntity<ResponseMessage> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.NOT_FOUND,responseEntityUpdate.getStatusCode());

    }

    @Test
    //Share a Report when the user is not in the DB
    void ShareReportNoUserFoundError () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,Object> map = getMap();

        ReportDTO reportDTOShare = createReport();

        reportDTOShare.setCompanyUserName("TerryNot");
        reportDTOShare.setCompany("TestCompanyNot");
        reportDTOShare.setEmail("TerryNot@email.com");

        UriTemplate uriTemplateShare = new UriTemplate("/api/dashboard/shareReport?name={name}&companyName={companyName}&email={email}");
        URI uriUpdate = uriTemplateShare.expand(map);

        RequestEntity requestEntityUpdate = new RequestEntity(reportDTOShare, headers, HttpMethod.POST, uriUpdate);

        ResponseEntity<ResponseMessage> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate, ResponseMessage.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntityUpdate.getStatusCode());

    }

    @Test
    void saveReportsAndUpdateUnautorized () throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ReportDTO reportDTO = createReport();
        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&companyName={companyName}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity, ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uriTemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&companyName={companyName}&email={email}");
        URI uriGet = uriTemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());


        // ############# Delete API ##############

        CompanyUser companyUser = new CompanyUser();
        companyUser.setName("Not John");
        companyUser.setCompanyName("TestCompany");
        companyUser.setEmail("Terry@email.com");

        companyUserRepository.save(companyUser);

        UriTemplate uriTemplateUpdate = new UriTemplate("/api/dashboard/updateReports?name={name}&companyName={companyName}&email={email}");
        map.put("name","Not John");
        map.put("email","Terry@email.com");
        URI uriUpdate = uriTemplateUpdate.expand(map);

        RequestEntity request = new RequestEntity(reportDTOSize.get(0),headers,HttpMethod.PUT,uriUpdate);

        ResponseEntity responseEntityDelete = testRestTemplate.exchange(request, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.UNAUTHORIZED,responseEntityDelete.getStatusCode());

    }

}


