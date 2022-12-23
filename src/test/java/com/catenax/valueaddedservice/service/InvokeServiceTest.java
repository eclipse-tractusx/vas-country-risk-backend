package com.catenax.valueaddedservice.service;


import com.catenax.valueaddedservice.ValueAddedServiceApplication;
import com.catenax.valueaddedservice.service.logic.InvokeService;
import com.catenax.valueaddedservice.service.mocks.ConfigServerMock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = ValueAddedServiceApplication.class)
class InvokeServiceTest {


    public static WireMockServer wireMockServer;

    @Autowired
    private InvokeService invokeService;

    @BeforeEach
    public void preSetup() throws InterruptedException {
        wireMockServer = ConfigServerMock.openPort(wireMockServer);
    }

    @AfterEach
    public void afterAll() throws InterruptedException {
        wireMockServer = ConfigServerMock.closePort(wireMockServer);
    }


    @Test
    @DisplayName("Should return the response when the request is success")
    void executeRequestWhenRequestIsSuccessThenReturnResponse() throws JsonProcessingException {
        String url = "http://localhost:8585/api/v1/users";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        List<String> list = new ArrayList<>();
        list.add("oneElement");
        ConfigServerMock.mocKConnectionToExternalBpnApi(wireMockServer, list);
        ResponseEntity<List> response =
                (ResponseEntity<List>) invokeService.executeRequest(url, HttpMethod.GET, httpEntity, list);
        assertNotEquals(0, response.getBody().size());
    }

    @Test
    @DisplayName("Should return empty list when the request is failed")
    void executeRequestWhenRequestIsFailedThenReturnEmptyList() throws JsonProcessingException {
        String url = "http://localhost:8585/api/v1/usersError";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        List<String> list = new ArrayList<>();
        list.add("oneElement");
        ConfigServerMock.mocKConnectionToExternalBpnApiError(wireMockServer);
        List newEmptyList = (List) invokeService.executeRequest(url, HttpMethod.GET, httpEntity, list);
        assertEquals(0, newEmptyList.size());
    }
}