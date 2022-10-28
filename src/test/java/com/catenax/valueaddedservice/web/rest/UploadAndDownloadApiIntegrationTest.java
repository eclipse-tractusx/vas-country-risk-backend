package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.service.csv.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,classes = ValueAddedServiceApplication.class)
class UploadAndDownloadApiIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;


    private Map<String,Object> getMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("company","TestCompany");
        map.put("name","John");
        map.put("email","John@email.com");

        return map;
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
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(Files.readAllBytes(Paths.get("src/test/resources/config/liquibase/test-data/file_test_upload.csv")), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, uri);

        ResponseEntity<ResponseMessage> responseEntity = testRestTemplate.exchange(requestEntity,ResponseMessage.class);

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());


        // TODO test de erro no nome duplicado nao deve ser feito no mesmo teste unitario
        // TODO AQUI deves fazer um pedido ratyngs by year e validar que tem o rating que acabaste de inserir

    }

    @Test
    @Transactional
    void getDataSourceTemplate() throws Exception {

        UriTemplate uritemplate= new UriTemplate("/api/dashboard/getTemplate?");
        URI uri = uritemplate.expand();
        RequestEntity<Void> request = RequestEntity
                .get(uri).accept(MediaType.APPLICATION_OCTET_STREAM).build();
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(request,String.class);
        String Data =  responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertNotEquals(0,Data.length());
    }

    @Test
    @Transactional
    void errorOnUpload(){

        // TODO fazer um upload duas vezes e obter erro em vez de sucesso
        assertNotEquals(0,1);
    }


}


