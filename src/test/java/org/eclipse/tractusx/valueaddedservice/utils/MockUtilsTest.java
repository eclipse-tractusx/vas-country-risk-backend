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
package org.eclipse.tractusx.valueaddedservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.service.mocks.ConfigServerMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MockUtilsTest {
    public static WireMockServer wireMockServer;

    @BeforeAll
    public static void beforeAll() {
        try {
            log.info("Opening Mock Server");
            wireMockServer = ConfigServerMock.openPort(wireMockServer);
        } catch (InterruptedException e) {
            log.info("Error on opening port {} {}",e.getMessage(),e.getCause());
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void openPorts() throws JsonProcessingException {
        List<String> list = new ArrayList<>();
        list.add("oneElement");
        ConfigServerMock.mocKConnectionToBpdmGateAddresses(wireMockServer);
        ConfigServerMock.mocKConnectionToBpdmGateLegalEntity(wireMockServer);
        ConfigServerMock.mocKConnectionToBpdmGateSite(wireMockServer);
        ConfigServerMock.mocKConnectionToExternalBpnApi(wireMockServer, list);
        ConfigServerMock.mocKConnectionToExternalBpnApiError(wireMockServer);
    }

}
