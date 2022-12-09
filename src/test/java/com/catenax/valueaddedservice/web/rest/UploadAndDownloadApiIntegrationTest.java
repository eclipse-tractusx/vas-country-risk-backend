package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.constants.VasConstants;
import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.repository.DataSourceRepository;
import com.catenax.valueaddedservice.repository.DataSourceValueRepository;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import com.catenax.valueaddedservice.service.mapper.DataSourceMapper;
import lombok.extern.slf4j.Slf4j;
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
        headers.set("type", VasConstants.CSV_ROLE_TYPE);

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

        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTemplate?");
        URI uri = uritemplate.expand();
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
        headers.set("type", VasConstants.CSV_ROLE_TYPE);


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

    private void cleanDataSources(DataSourceDTO dataSourceDTO) {


        List<DataSourceValue> deleteList = dataSourceValueRepository.findByDataSource(dataSourceMapper.toEntity(dataSourceDTO));

        assertNotEquals(0,deleteList.size());

        dataSourceValueRepository.deleteAll(deleteList);

        dataSourceRepository.deleteById(dataSourceDTO.getId());
    }


}


