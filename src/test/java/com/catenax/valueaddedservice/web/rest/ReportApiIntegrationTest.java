package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.repository.ReportValuesRepository;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
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

    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("companyName","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");
        map.put("ratingName", "testRating123");

        return map;
    }

    @AfterEach
    public void cleanReports(){
        reportValuesRepository.deleteAll();
        reportRepository.deleteAll();

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

        List<String> list = new ArrayList<>();
        list.add("value");
        List<ReportValuesDTO> reportValuesDTOList1 = new ArrayList<>();
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("TestReportTest");
        reportDTO.setCompanyUserName("John");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("Test Company");

        reportValuesDTO.setObjectValue(list);
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
}


