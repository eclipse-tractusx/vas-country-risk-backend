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
package org.eclipse.tractusx.valueaddedservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.service.logic.InvokeService;
import org.eclipse.tractusx.valueaddedservice.service.mocks.ConfigServerMock;
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
        String url = "http://localhost:8585/api/dashboard/v1/users";
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
        String url = "http://localhost:8585/api/dashboard/v1/usersError";
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