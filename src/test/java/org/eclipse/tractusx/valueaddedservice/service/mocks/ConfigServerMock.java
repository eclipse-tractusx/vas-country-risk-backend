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
package org.eclipse.tractusx.valueaddedservice.service.mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ConfigServerMock {

    public static WireMockServer wireMockServer = new WireMockServer(8585);
    public static boolean flag = true;
    private static final Object lock = new Object();

    public static WireMockServer openPort(WireMockServer server) throws InterruptedException {

        synchronized (lock) {
            if (!wireMockServer.isRunning())
                wireMockServer.start();
            server = wireMockServer;
        }
        //Opening Port one time only, the first thread opens the port
        return server;
    }

    public static WireMockServer closePort(WireMockServer server) {
        wireMockServer = server;
        wireMockServer.stop();
        wireMockServer.shutdown();
        flag = false;
        return wireMockServer;
    }

    public static void mocKConnectionToExternalBpnApi(WireMockServer mockServer, List list) throws JsonProcessingException {

        mockServer.stubFor(get("/api/dashboard/v1/users")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(list))));
    }

    public static void mocKConnectionToExternalBpnApiError(WireMockServer mockServer) {

        mockServer.stubFor(get("/api/dashboard/v1/usersError")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)));
    }

    public static void mocKConnectionToBpdmGateAddresses(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/companies/test-company/api/catena/output/addresses/search")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "    \"totalElements\": 1,\n" +
                                "    \"totalPages\": 1,\n" +
                                "    \"page\": 0,\n" +
                                "    \"contentSize\": 1,\n" +
                                "    \"content\": [\n" +
                                "        {\n" +
                                "            \"nameParts\": [\n" +
                                "                \"WESTERN_LATIN_STANDARD\"\n" +
                                "            ],\n" +
                                "            \"states\": [\n" +
                                "                {\n" +
                                "                    \"description\": \"WESTERN_LATIN_STANDARD\",\n" +
                                "                    \"validFrom\": \"2020-12-16T05:54:48.942\",\n" +
                                "                    \"validTo\": \"2023-06-05T07:31:01.213\",\n" +
                                "                    \"type\": \"ACTIVE\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"identifiers\": [\n" +
                                "                {\n" +
                                "                    \"value\": \"12345678910\",\n" +
                                "                    \"type\": \"CUSTOM_ID_ADD_TYPE\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"physicalPostalAddress\": {\n" +
                                "                \"geographicCoordinates\": {\n" +
                                "                    \"longitude\": 0.0,\n" +
                                "                    \"latitude\": 0.0,\n" +
                                "                    \"altitude\": 0.0\n" +
                                "                },\n" +
                                "                \"country\": \"DE\",\n" +
                                "                \"administrativeAreaLevel1\": \"Baden-Wuerttemberg\",\n" +
                                "                \"administrativeAreaLevel2\": \"test1\",\n" +
                                "                \"administrativeAreaLevel3\": \"test2\",\n" +
                                "                \"postalCode\": \"70546\",\n" +
                                "                \"city\": \"Stuttgart\",\n" +
                                "                \"district\": \"Stuttgart\",\n" +
                                "                \"street\": {\n" +
                                "                    \"namePrefix\": \"test prefix\",\n" +
                                "                    \"additionalNamePrefix\": \"test1\",\n" +
                                "                    \"name\": \"Stuttgarter Strasse\",\n" +
                                "                    \"nameSuffix\": \"test\",\n" +
                                "                    \"additionalNameSuffix\": \"test2\",\n" +
                                "                    \"houseNumber\": \"1\",\n" +
                                "                    \"milestone\": \"Stuttgarter Strasse 1\",\n" +
                                "                    \"direction\": \"Stuttgarter Str.\"\n" +
                                "                },\n" +
                                "                \"companyPostalCode\": \"GM01\",\n" +
                                "                \"industrialZone\": \"HEADQUARTER\",\n" +
                                "                \"building\": \"Building A\",\n" +
                                "                \"floor\": \"A\",\n" +
                                "                \"door\": \"test\"\n" +
                                "            },\n" +
                                "            \"alternativePostalAddress\": {\n" +
                                "                \"geographicCoordinates\": {\n" +
                                "                    \"longitude\": 0.0,\n" +
                                "                    \"latitude\": 0.0,\n" +
                                "                    \"altitude\": 0.0\n" +
                                "                },\n" +
                                "                \"country\": \"DE\",\n" +
                                "                \"administrativeAreaLevel1\": \"Baden-Wuerttemberg 1\",\n" +
                                "                \"postalCode\": \"1234\",\n" +
                                "                \"city\": \"Stuttgart 1\",\n" +
                                "                \"deliveryServiceType\": \"PO_BOX\",\n" +
                                "                \"deliveryServiceQualifier\": \"test\",\n" +
                                "                \"deliveryServiceNumber\": \"1234\"\n" +
                                "            },\n" +
                                "            \"roles\": [\n" +
                                "                \"SUPPLIER\"\n" +
                                "            ],\n" +
                                "            \"externalId\": \"12044444_legalAddress\",\n" +
                                "            \"legalEntityExternalId\": \"12044444\",\n" +
                                "            \"siteExternalId\": null,\n" +
                                "            \"bpna\": \"BPNL0000000001YN\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));
    }

    public static void mocKConnectionToBpdmGateSite(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/companies/test-company/api/catena/output/sites/search")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "    \"totalElements\": 1,\n" +
                                "    \"totalPages\": 1,\n" +
                                "    \"page\": 0,\n" +
                                "    \"contentSize\": 1,\n" +
                                "    \"content\": [\n" +
                                "        {\n" +
                                "            \"nameParts\": [\n" +
                                "                \"Factory UT\"\n" +
                                "            ],\n" +
                                "            \"states\": [\n" +
                                "                {\n" +
                                "                    \"description\": \"Baden-Wuerttemberg\",\n" +
                                "                    \"validFrom\": \"2023-06-05T08:29:20.015\",\n" +
                                "                    \"validTo\": \"2023-06-05T08:29:20.015\",\n" +
                                "                    \"type\": \"ACTIVE\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"roles\": [\n" +
                                "                \"SUPPLIER\"\n" +
                                "            ],\n" +
                                "            \"mainAddress\": {\n" +
                                "                \"nameParts\": [\n" +
                                "                    \"WESTERN_LATIN_STANDARD\"\n" +
                                "                ],\n" +
                                "                \"states\": [\n" +
                                "                    {\n" +
                                "                        \"description\": \"Baden-Wuerttemberg\",\n" +
                                "                        \"validFrom\": \"2023-06-05T08:29:20.015\",\n" +
                                "                        \"validTo\": \"2023-06-05T08:29:20.015\",\n" +
                                "                        \"type\": \"ACTIVE\"\n" +
                                "                    }\n" +
                                "                ],\n" +
                                "                \"identifiers\": [\n" +
                                "                    {\n" +
                                "                        \"value\": \"123456789\",\n" +
                                "                        \"type\": \"CUSTOM_SITE_ID\"\n" +
                                "                    }\n" +
                                "                ],\n" +
                                "                \"physicalPostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": {\n" +
                                "                        \"longitude\": 0.0,\n" +
                                "                        \"latitude\": 0.0,\n" +
                                "                        \"altitude\": 0.0\n" +
                                "                    },\n" +
                                "                    \"country\": \"DE\",\n" +
                                "                    \"administrativeAreaLevel1\": \"Baden-Wuerttember\",\n" +
                                "                    \"administrativeAreaLevel2\": null,\n" +
                                "                    \"administrativeAreaLevel3\": null,\n" +
                                "                    \"postalCode\": \"70327\",\n" +
                                "                    \"city\": \"Stuttgart\",\n" +
                                "                    \"district\": \"Stuttgart\",\n" +
                                "                    \"street\": {\n" +
                                "                        \"namePrefix\": \"test prefix\",\n" +
                                "                        \"additionalNamePrefix\": \"test\",\n" +
                                "                        \"name\": \"Untertuerckheim Strasse 1\",\n" +
                                "                        \"nameSuffix\": \"test1 suffix\",\n" +
                                "                        \"additionalNameSuffix\": \"test suffix\",\n" +
                                "                        \"houseNumber\": \"1234\",\n" +
                                "                        \"milestone\": \"Untertuerckheim Strasse 1\",\n" +
                                "                        \"direction\": \"Untertuerckheim Strasse 1\"\n" +
                                "                    },\n" +
                                "                    \"companyPostalCode\": \"71034\",\n" +
                                "                    \"industrialZone\": \"Sindelfinden\",\n" +
                                "                    \"building\": \"Building A\",\n" +
                                "                    \"floor\": \"A\",\n" +
                                "                    \"door\": \"1\"\n" +
                                "                },\n" +
                                "                \"alternativePostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": {\n" +
                                "                        \"longitude\": 0.0,\n" +
                                "                        \"latitude\": 0.0,\n" +
                                "                        \"altitude\": 0.0\n" +
                                "                    },\n" +
                                "                    \"country\": \"DE\",\n" +
                                "                    \"administrativeAreaLevel1\": \"Stuttgart\",\n" +
                                "                    \"postalCode\": \"1234\",\n" +
                                "                    \"city\": \"Stuttgart\",\n" +
                                "                    \"deliveryServiceType\": \"PO_BOX\",\n" +
                                "                    \"deliveryServiceQualifier\": \"test qualifier\",\n" +
                                "                    \"deliveryServiceNumber\": \"Untertuerckheim Strasse 1\"\n" +
                                "                },\n" +
                                "                \"roles\": [\n" +
                                "                    \"SUPPLIER\"\n" +
                                "                ],\n" +
                                "                \"externalId\": \"12044444_site\",\n" +
                                "                \"legalEntityExternalId\": null,\n" +
                                "                \"siteExternalId\": \"12044444\",\n" +
                                "                \"bpna\": \"BPNS000000000001\"\n" +
                                "            },\n" +
                                "            \"externalId\": \"12044444\",\n" +
                                "            \"legalEntityExternalId\": \"12044444\",\n" +
                                "            \"bpns\": \"BPNL0000000001YN\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));
    }

    public static void mocKConnectionToBpdmGateLegalEntity(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/companies/test-company/api/catena/output/legal-entities/search")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "    \"totalElements\": 1,\n" +
                                "    \"totalPages\": 1,\n" +
                                "    \"page\": 0,\n" +
                                "    \"contentSize\": 1,\n" +
                                "    \"content\": [\n" +
                                "        {\n" +
                                "            \"legalNameParts\": [\n" +
                                "                \"Name of Company\"\n" +
                                "            ],\n" +
                                "            \"identifiers\": [\n" +
                                "                {\n" +
                                "                    \"value\": \"123456789\",\n" +
                                "                    \"type\": \"CUSTOM_ID_TYPE\",\n" +
                                "                    \"issuingBody\": \"CUSTOM_ISSUE_BODY\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"legalShortName\": \"NoC\",\n" +
                                "            \"legalForm\": \"CUSTOM_LEGAL_FORM\",\n" +
                                "            \"states\": [\n" +
                                "                {\n" +
                                "                    \"description\": null,\n" +
                                "                    \"validFrom\": \"2020-12-16T05:54:48.942\",\n" +
                                "                    \"validTo\": \"2023-06-05T07:31:01.213\",\n" +
                                "                    \"type\": \"ACTIVE\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"classifications\": [\n" +
                                "                {\n" +
                                "                    \"value\": \"Farming of cattle, dairy farming\",\n" +
                                "                    \"code\": \"01.21\",\n" +
                                "                    \"type\": \"NACE\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"roles\": [\n" +
                                "                \"SUPPLIER\"\n" +
                                "            ],\n" +
                                "            \"legalAddress\": {\n" +
                                "                \"nameParts\": [\n" +
                                "                    \"WESTERN_LATIN_STANDARD\"\n" +
                                "                ],\n" +
                                "                \"states\": [\n" +
                                "                    {\n" +
                                "                        \"description\": \"WESTERN_LATIN_STANDARD\",\n" +
                                "                        \"validFrom\": \"2020-12-16T05:54:48.942\",\n" +
                                "                        \"validTo\": \"2023-06-05T07:31:01.213\",\n" +
                                "                        \"type\": \"ACTIVE\"\n" +
                                "                    }\n" +
                                "                ],\n" +
                                "                \"identifiers\": [\n" +
                                "                    {\n" +
                                "                        \"value\": \"12345678910\",\n" +
                                "                        \"type\": \"CUSTOM_ID_ADD_TYPE\"\n" +
                                "                    }\n" +
                                "                ],\n" +
                                "                \"physicalPostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": {\n" +
                                "                        \"longitude\": 0.0,\n" +
                                "                        \"latitude\": 0.0,\n" +
                                "                        \"altitude\": 0.0\n" +
                                "                    },\n" +
                                "                    \"country\": \"DE\",\n" +
                                "                    \"administrativeAreaLevel1\": \"Baden-Wuerttemberg\",\n" +
                                "                    \"administrativeAreaLevel2\": \"test1\",\n" +
                                "                    \"administrativeAreaLevel3\": \"test2\",\n" +
                                "                    \"postalCode\": \"70546\",\n" +
                                "                    \"city\": \"Stuttgart\",\n" +
                                "                    \"district\": \"Stuttgart\",\n" +
                                "                    \"street\": {\n" +
                                "                        \"namePrefix\": \"test prefix\",\n" +
                                "                        \"additionalNamePrefix\": \"test1\",\n" +
                                "                        \"name\": \"Stuttgarter Strasse\",\n" +
                                "                        \"nameSuffix\": \"test\",\n" +
                                "                        \"additionalNameSuffix\": \"test2\",\n" +
                                "                        \"houseNumber\": \"1\",\n" +
                                "                        \"milestone\": \"Stuttgarter Strasse 1\",\n" +
                                "                        \"direction\": \"Stuttgarter Str.\"\n" +
                                "                    },\n" +
                                "                    \"companyPostalCode\": \"GM01\",\n" +
                                "                    \"industrialZone\": \"HEADQUARTER\",\n" +
                                "                    \"building\": \"Building A\",\n" +
                                "                    \"floor\": \"A\",\n" +
                                "                    \"door\": \"test\"\n" +
                                "                },\n" +
                                "                \"alternativePostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": {\n" +
                                "                        \"longitude\": 0.0,\n" +
                                "                        \"latitude\": 0.0,\n" +
                                "                        \"altitude\": 0.0\n" +
                                "                    },\n" +
                                "                    \"country\": \"DE\",\n" +
                                "                    \"administrativeAreaLevel1\": \"Baden-Wuerttemberg 1\",\n" +
                                "                    \"postalCode\": \"1234\",\n" +
                                "                    \"city\": \"Stuttgart 1\",\n" +
                                "                    \"deliveryServiceType\": \"PO_BOX\",\n" +
                                "                    \"deliveryServiceQualifier\": \"test\",\n" +
                                "                    \"deliveryServiceNumber\": \"1234\"\n" +
                                "                },\n" +
                                "                \"roles\": [\n" +
                                "                    \"SUPPLIER\"\n" +
                                "                ],\n" +
                                "                \"externalId\": \"12044444_legalAddress\",\n" +
                                "                \"legalEntityExternalId\": \"12044444\",\n" +
                                "                \"siteExternalId\": null,\n" +
                                "                \"bpna\": \"BPNL0000000001YN\"\n" +
                                "            },\n" +
                                "            \"externalId\": \"12044444\",\n" +
                                "            \"bpnl\": \"BPNL0000000001YN\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));
    }





}
