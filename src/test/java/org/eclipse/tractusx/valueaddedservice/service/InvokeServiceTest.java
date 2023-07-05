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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.ValueAddedServiceApplication;
import org.eclipse.tractusx.valueaddedservice.service.logic.InvokeService;
import org.eclipse.tractusx.valueaddedservice.utils.MockUtilsTest;
import org.eclipse.tractusx.valueaddedservice.utils.PostgreSQLContextInitializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = ValueAddedServiceApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = PostgreSQLContextInitializer.class)
@Slf4j
class InvokeServiceTest {


    @Autowired
    InvokeService invokeService;

    MockUtilsTest mockUtilsTest;

    @BeforeAll
    public void beforeAll() {
        mockUtilsTest = new MockUtilsTest();
        mockUtilsTest.beforeAll();
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException, InterruptedException {
        mockUtilsTest.openPorts();
    }

    @Test
    @DisplayName("Should return the response when the request is success")
    void executeRequestWhenRequestIsSuccessThenReturnResponse() throws JsonProcessingException {
        String url = "http://localhost:8585/api/dashboard/v1/users";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        List<String> response =
                invokeService.executeRequest(url, HttpMethod.GET, httpEntity, String.class, this::mockMappingFunction);
        assertNotEquals(0, response.size());
    }

    @Test
    @DisplayName("Should return empty list when the request is failed")
    void executeRequestWhenRequestIsFailedThenReturnEmptyList() throws JsonProcessingException {
        String url = "http://localhost:8585/api/dashboard/v1/usersError";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        List<String> newEmptyList = invokeService.executeRequest(url, HttpMethod.GET, httpEntity, String.class, this::mockMappingFunction);
        assertEquals(0, newEmptyList.size());
    }

    // Mock mapping function
    private List<String> mockMappingFunction(String json) {
        // Implement your mock mapping logic here

        List<String> list = new ArrayList<>();
        list.add("oneElement");
        return list;
    }


}