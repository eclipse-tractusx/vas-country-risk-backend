package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
@ActiveProfiles(profiles = "test")
class DashBoardResourceIntegrationTest {


    @Value(value = "classpath:config/liquibase/test-data/ListRating.json")
    private Resource listRatingJson;

    @Value(value = "classpath:config/liquibase/test-data/ListReportValue.json")
    private Resource listReportJson;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ObjectMapper objectMapper;

    Long ReportId;

    private Map<String,Object> getMap() throws IOException {
        List<RatingDTO> ratingDTOS = objectMapper.readValue(listRatingJson.getInputStream(), new TypeReference<List<RatingDTO>>() {
        });

        Map<String,Object> map = new HashMap<>();
        map.put("year",2021);
        map.put("ratings",objectMapper.writeValueAsString(ratingDTOS));
        map.put("company","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        map.put("ratingName", "testRating123");

        return map;
    }

    private Map<String,Object> getMapDifferentUser() throws IOException {

        Map<String,Object> map = new HashMap<>();
        map.put("company","Different");
        map.put("name","DifferentName");
        map.put("email","Different@email.com");

        return map;
    }

    private ReportDTO globalReportDTO() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ReportValuesDTO> reportValuesDTOList1 = new ArrayList<>();

        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setObjectValue("ReportTest");

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("TestReportTest");
        reportDTO.setCompanyUserName("John");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("Test Company");

        reportValuesDTO.setObjectValue(headers);
        reportValuesDTOList1.add(reportValuesDTO);
        reportDTO.setReportValuesDTOList(reportValuesDTOList1);

        return reportDTO;
    }

    @Test
    @Transactional
    void getTableInfo() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTableInfo?year={year}&ratings={ratings}&name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<DashBoardTableDTO> list = (List<DashBoardTableDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getWorldMapInfo() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getWorldMap?year={year}&ratings={ratings}&name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<DashBoardWorldMapDTO> list = (List<DashBoardWorldMapDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getDataSourceTemplate() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTemplate?");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).accept(MediaType.APPLICATION_OCTET_STREAM).build();
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(request,String.class);
        String Data =  responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,Data.length());
    }

    @Test
    @Transactional
    void uploadCsv() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/uploadCsv?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("ratingName", "testeRating");

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename("testeFile.csv")
                .build();

        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get("src/test/java/com/catenax/valueaddedservice/web/rest/a728d387-017b-4cf6-a1d6-8467cbcae022.csv")), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity,String.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Duplicated File Name Test
        RequestEntity requestEntityDuplicate = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntityDuplicate = testRestTemplate.exchange(requestEntityDuplicate,String.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntityDuplicate.getStatusCode());

        //No File Inserted
        MultiValueMap<String, Object> bodyNoFile = new LinkedMultiValueMap<>();
        body.add("file", null);

        RequestEntity requestEntityNoFile = new RequestEntity(bodyNoFile, headers, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntityNoFile = testRestTemplate.exchange(requestEntityNoFile,String.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntityNoFile.getStatusCode());
    }

    @Test
    @Transactional
    void getCountryByAssociatedBPtoUser() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getBpnCountrys?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<CountryDTO> list = (List<CountryDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getCompanyBpns() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCompanyBpns?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<CompanyUserDTO> list = (List<CompanyUserDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void allYears() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/allYears?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<Integer> list = (List<Integer>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void ratingsByYear() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/ratingsByYear?year={year}&name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<DataSourceDTO> list = (List<DataSourceDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getUserRanges() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getUserRanges?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<RangeDTO> list = (List<RangeDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());

        //Get Ranges with User that has no Saved Ranges
        Map<String,Object> mapDifferendUser = getMapDifferentUser();
        URI uriDif = uritemplate.expand(mapDifferendUser);
        RequestEntity<Void> requestDif = RequestEntity
                .get(uriDif).build();
        ResponseEntity<Object> responseEntityDif = testRestTemplate.exchange(requestDif,Object.class);
        List<RangeDTO> listDif = (List<RangeDTO>) responseEntityDif.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,listDif.size());

    }

    @Test
    @Transactional
    void getCountryFilterByISO2() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getCountryFilterByISO2?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<CountryDTO> list = (List<CountryDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }

    @Test
    @Transactional
    void getBpnCountrys() throws Exception {

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getBpnCountrys?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);
        RequestEntity<Void> request = RequestEntity
                .get(uri).build();
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(request,Object.class);
        List<CountryDTO> list = (List<CountryDTO>) responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,list.size());
    }


    @Test
    @Transactional
    void saveReports () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportDTO reportDTO = globalReportDTO();

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(requestEntity,Object.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uritemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&company={company}&email={email}");
        URI uriGet = uritemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<List<ReportDTO>>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        //################## Duplicated Name on Report ##############
        RequestEntity requestEntityDupe = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<String> responseEntityDupe = testRestTemplate.exchange(requestEntityDupe,String.class);

        assertEquals(HttpStatus.BAD_REQUEST,responseEntityDupe.getStatusCode());
    }


    @Test
    @Transactional
    void getReportsValueByReport () throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportDTO reportDTO = globalReportDTO();

        reportDTO.setReportName("OutroReport"); //Changed Name of the Report

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveReports?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(reportDTO, headers, HttpMethod.POST, uri);

        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(requestEntity,Object.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        // ############# Get API ##############
        UriTemplate uritemplateGet = new UriTemplate("/api/dashboard/getReportsByCompanyUser?name={name}&company={company}&email={email}");
        URI uriGet = uritemplateGet.expand(map);

        RequestEntity requestEntityGet = new RequestEntity(HttpMethod.GET, uriGet);

        ResponseEntity<List<ReportDTO>> responseEntityGet = testRestTemplate.exchange(requestEntityGet, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK,responseEntityGet.getStatusCode());

        List<ReportDTO> reportDTOSize = responseEntityGet.getBody();

        assertNotEquals(0,reportDTOSize.size());

        reportDTO.setId(reportDTOSize.get(0).getId());

        // ######## ReportsByReport #######
        UriTemplate uritemplateByReport= new UriTemplate("/api/dashboard/getReportsValueByReport?id={id}&reportName={reportName}&companyUserName={companyUserName}" +
                "&company={company}&type={type}&reportValues={reportValues}");

        Map<String,Object> mapByReport = new HashMap<>();
        mapByReport.put("id",reportDTO.getId());
        mapByReport.put("reportName",reportDTO.getReportName());
        mapByReport.put("companyUserName",reportDTO.getCompanyUserName());
        mapByReport .put("company",reportDTO.getCompany());
        mapByReport.put("type",reportDTO.getType());
        mapByReport.put("reportValues", reportDTO.getReportValuesDTOList());

        URI uriByReport = uritemplateByReport.expand(mapByReport);
        RequestEntity<Void> requestByReport = RequestEntity
                .get(uriByReport).build();
        ResponseEntity<Object> responseEntityByReport = testRestTemplate.exchange(requestByReport,Object.class);
        List<ReportValuesDTO> listByReport = (List<ReportValuesDTO>) responseEntityByReport.getBody();

        assertEquals(HttpStatus.OK,responseEntityByReport.getStatusCode());
        assertNotEquals(0,listByReport.size());
    }

    @Test
    @Transactional
    void saveUserRanges() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<RangeDTO> rangeDTOList = new ArrayList<>();

        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");

        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setRange(RangeType.Max);
        rangeDTO.setValue(80);
        rangeDTO.setDescription("Max Value");
        rangeDTO.setCompanyUser(companyUserDTO);

        rangeDTOList.add(rangeDTO);

        Map<String,Object> map = getMap();
        UriTemplate uritemplate= new UriTemplate("/api/dashboard/saveUserRanges?name={name}&company={company}&email={email}");
        URI uri = uritemplate.expand(map);

        RequestEntity requestEntity = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(requestEntity,Object.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

        //Update Ranges
        rangeDTO.setValue(70); //Changed Value for Max Range

        RequestEntity requestEntityUpdate = new RequestEntity(rangeDTOList, headers, HttpMethod.POST, uri);

        ResponseEntity<Object> responseEntityUpdate = testRestTemplate.exchange(requestEntityUpdate,Object.class);

        assertEquals(HttpStatus.OK,responseEntityUpdate.getStatusCode());
    }

}


