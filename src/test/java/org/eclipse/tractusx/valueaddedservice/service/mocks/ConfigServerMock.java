/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG 
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
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

        mockServer.stubFor(post("/pool/api/catena/addresses/search")
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
                                "            \"bpna\": \"BPNA000000006X3N\",\n" +
                                "            \"name\": \"E2E Tests 1223 Generic Endpoint Company 1\",\n" +
                                "            \"states\": [],\n" +
                                "            \"identifiers\": [],\n" +
                                "            \"physicalPostalAddress\": {\n" +
                                "                \"geographicCoordinates\": null,\n" +
                                "                \"country\": {\n" +
                                "                    \"technicalKey\": \"DE\",\n" +
                                "                    \"name\": \"Germany\"\n" +
                                "                },\n" +
                                "                \"administrativeAreaLevel1\": null,\n" +
                                "                \"administrativeAreaLevel2\": null,\n" +
                                "                \"administrativeAreaLevel3\": null,\n" +
                                "                \"postalCode\": \"70546\",\n" +
                                "                \"city\": \"Stuttgart\",\n" +
                                "                \"district\": null,\n" +
                                "                \"street\": {\n" +
                                "                    \"name\": \"Stuttgarter Strasse\",\n" +
                                "                    \"houseNumber\": \"1\",\n" +
                                "                    \"milestone\": null,\n" +
                                "                    \"direction\": null\n" +
                                "                },\n" +
                                "                \"companyPostalCode\": null,\n" +
                                "                \"industrialZone\": null,\n" +
                                "                \"building\": null,\n" +
                                "                \"floor\": null,\n" +
                                "                \"door\": null\n" +
                                "            },\n" +
                                "            \"alternativePostalAddress\": null,\n" +
                                "            \"bpnLegalEntity\": null,\n" +
                                "            \"isLegalAddress\": false,\n" +
                                "            \"bpnSite\": \"BPNS0000000001YN\",\n" +
                                "            \"isMainAddress\": true,\n" +
                                "            \"createdAt\": \"2023-11-14T18:16:30.076772Z\",\n" +
                                "            \"updatedAt\": \"2023-11-14T18:16:30.080344Z\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));
    }

    public static void mocKConnectionToBpdmGateSite(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/pool/api/catena/sites/search")
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
                                "            \"bpns\": \"BPNS0000000001YN\",\n" +
                                "            \"name\": \"E2E Tests 1223 Generic Endpoint Company 1\",\n" +
                                "            \"states\": [],\n" +
                                "            \"bpnLegalEntity\": \"BPNL00000007QTKE\",\n" +
                                "            \"createdAt\": \"2023-11-14T18:16:30.077820Z\",\n" +
                                "            \"updatedAt\": \"2023-11-14T18:16:30.077823Z\",\n" +
                                "            \"mainAddress\": {\n" +
                                "                \"bpna\": \"BPNA000000006X3N\",\n" +
                                "                \"name\": \"E2E Tests 1223 Generic Endpoint Company 1\",\n" +
                                "                \"states\": [],\n" +
                                "                \"identifiers\": [],\n" +
                                "                \"physicalPostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": null,\n" +
                                "                    \"country\": {\n" +
                                "                        \"technicalKey\": \"DE\",\n" +
                                "                        \"name\": \"Germany\"\n" +
                                "                    },\n" +
                                "                    \"administrativeAreaLevel1\": null,\n" +
                                "                    \"administrativeAreaLevel2\": null,\n" +
                                "                    \"administrativeAreaLevel3\": null,\n" +
                                "                    \"postalCode\": \"70546\",\n" +
                                "                    \"city\": \"Stuttgart\",\n" +
                                "                    \"district\": null,\n" +
                                "                    \"street\": {\n" +
                                "                        \"name\": \"Stuttgarter Strasse\",\n" +
                                "                        \"houseNumber\": \"1\",\n" +
                                "                        \"milestone\": null,\n" +
                                "                        \"direction\": null\n" +
                                "                    },\n" +
                                "                    \"companyPostalCode\": null,\n" +
                                "                    \"industrialZone\": null,\n" +
                                "                    \"building\": null,\n" +
                                "                    \"floor\": null,\n" +
                                "                    \"door\": null\n" +
                                "                },\n" +
                                "                \"alternativePostalAddress\": null,\n" +
                                "                \"bpnLegalEntity\": null,\n" +
                                "                \"isLegalAddress\": false,\n" +
                                "                \"bpnSite\": \"BPNS0000000001YN\",\n" +
                                "                \"isMainAddress\": true,\n" +
                                "                \"createdAt\": \"2023-11-14T18:16:30.076772Z\",\n" +
                                "                \"updatedAt\": \"2023-11-14T18:16:30.080344Z\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")));
    }

    public static void mocKConnectionToBpdmGateLegalEntity(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/pool/api/catena/legal-entities/search")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "  \"totalElements\": 1,\n" +
                                "  \"totalPages\": 1,\n" +
                                "  \"page\": 1,\n" +
                                "  \"contentSize\": 1,\n" +
                                "  \"content\": [\n" +
                                "    {\n" +
                                "      \"bpnl\": \"BPNL00000007QZG8\",\n" +
                                "      \"legalName\": \"Showcase Business Partner Address\",\n" +
                                "      \"legalShortName\": null,\n" +
                                "      \"legalForm\": {\n" +
                                "        \"technicalKey\": \"UNKNOWN\",\n" +
                                "        \"name\": null,\n" +
                                "        \"abbreviation\": null\n" +
                                "      },\n" +
                                "      \"identifiers\": [],\n" +
                                "      \"states\": [],\n" +
                                "      \"relations\": [],\n" +
                                "      \"currentness\": \"2023-11-29T10:59:00.034673Z\",\n" +
                                "      \"confidenceCriteria\": {\n" +
                                "        \"sharedByOwner\": true,\n" +
                                "        \"checkedByExternalDataSource\": false,\n" +
                                "        \"numberOfSharingMembers\": 0,\n" +
                                "        \"lastConfidenceCheckAt\": \"2023-11-29T10:59:00.034673Z\",\n" +
                                "        \"nextConfidenceCheckAt\": \"2024-11-29T10:59:00.034673Z\",\n" +
                                "        \"confidenceLevel\": 1\n" +
                                "      },\n" +
                                "      \"isCatenaXMemberData\": true,\n" +
                                "      \"createdAt\": \"2023-11-29T10:59:00.047537Z\",\n" +
                                "      \"updatedAt\": \"2023-11-29T10:59:00.047543Z\",\n" +
                                "      \"legalAddress\": {\n" +
                                "        \"bpna\": \"BPNA000000007BT8\",\n" +
                                "        \"name\": \"Showcase Business Partner Address\",\n" +
                                "        \"states\": [],\n" +
                                "        \"identifiers\": [],\n" +
                                "        \"physicalPostalAddress\": {\n" +
                                "          \"geographicCoordinates\": null,\n" +
                                "          \"country\": {\n" +
                                "            \"technicalKey\": \"DE\",\n" +
                                "            \"name\": \"Germany\"\n" +
                                "          },\n" +
                                "          \"administrativeAreaLevel1\": {\n" +
                                "            \"countryCode\": \"DE\",\n" +
                                "            \"regionCode\": \"DE-BW\",\n" +
                                "            \"regionName\": \"Baden-Wurttemberg\"\n" +
                                "          },\n" +
                                "          \"administrativeAreaLevel2\": null,\n" +
                                "          \"administrativeAreaLevel3\": null,\n" +
                                "          \"postalCode\": \"77777\",\n" +
                                "          \"city\": \"City X\",\n" +
                                "          \"district\": \"\",\n" +
                                "          \"street\": {\n" +
                                "            \"name\": \"Base Street\",\n" +
                                "            \"houseNumber\": \"3\",\n" +
                                "            \"houseNumberSupplement\": null,\n" +
                                "            \"milestone\": \"\",\n" +
                                "            \"direction\": \"\",\n" +
                                "            \"namePrefix\": null,\n" +
                                "            \"additionalNamePrefix\": null,\n" +
                                "            \"nameSuffix\": null,\n" +
                                "            \"additionalNameSuffix\": null\n" +
                                "          },\n" +
                                "          \"companyPostalCode\": null,\n" +
                                "          \"industrialZone\": null,\n" +
                                "          \"building\": null,\n" +
                                "          \"floor\": null,\n" +
                                "          \"door\": null\n" +
                                "        },\n" +
                                "        \"alternativePostalAddress\": null,\n" +
                                "        \"bpnLegalEntity\": \"BPNL00000007QZG8\",\n" +
                                "        \"bpnSite\": null,\n" +
                                "        \"isCatenaXMemberData\": true,\n" +
                                "        \"createdAt\": \"2023-11-29T10:59:00.045497Z\",\n" +
                                "        \"updatedAt\": \"2023-11-29T10:59:00.059879Z\",\n" +
                                "        \"confidenceCriteria\": {\n" +
                                "          \"sharedByOwner\": true,\n" +
                                "          \"checkedByExternalDataSource\": false,\n" +
                                "          \"numberOfSharingMembers\": 0,\n" +
                                "          \"lastConfidenceCheckAt\": \"2023-11-29T10:59:00.045497Z\",\n" +
                                "          \"nextConfidenceCheckAt\": \"2024-11-29T10:59:00.045497Z\",\n" +
                                "          \"confidenceLevel\": 1\n" +
                                "        },\n" +
                                "        \"addressType\": \"LegalAndSiteMainAddress\"\n" +
                                "      }\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}\n")));
    }


    public static void mocKConnectionToBpdmGateGeneric(WireMockServer mockServer) throws JsonProcessingException {

        mockServer.stubFor(post("/companies/test-company/api/catena/output/business-partners/search")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("{\n" +
                                "    \"totalElements\": 4,\n" +
                                "    \"totalPages\": 1,\n" +
                                "    \"page\": 0,\n" +
                                "    \"contentSize\": 4,\n" +
                                "    \"content\": [\n" +
                                "  \n" +
                                "        {\n" +
                                "            \"externalId\": \"12237777\",\n" +
                                "            \"nameParts\": [\n" +
                                "                \"E2E Tests 1223 Generic Endpoint Company 1\"\n" +
                                "            ],\n" +
                                "            \"identifiers\": [\n" +
                                "                {\n" +
                                "                    \"type\": \"CUSTOM_ID_TYPE\",\n" +
                                "                    \"value\": \"12237777\",\n" +
                                "                    \"issuingBody\": \"CUSTOM_ISSUE_BODY\"\n" +
                                "                }\n" +
                                "            ],\n" +
                                "            \"states\": [],\n" +
                                "            \"roles\": [\n" +
                                "                \"SUPPLIER\"\n" +
                                "            ],\n" +
                                "            \"isOwnCompanyData\": false,\n" +
                                "            \"legalEntity\": {\n" +
                                "                \"legalEntityBpn\": \"BPNL00000007QTKE\",\n" +
                                "                \"legalName\": null,\n" +
                                "                \"shortName\": \"Generic Endpoint Company 1\",\n" +
                                "                \"legalForm\": null,\n" +
                                "                \"classifications\": [],\n" +
                                "                \"confidenceCriteria\": {\n" +
                                "                    \"sharedByOwner\": false,\n" +
                                "                    \"checkedByExternalDataSource\": false,\n" +
                                "                    \"numberOfBusinessPartners\": 1,\n" +
                                "                    \"lastConfidenceCheckAt\": \"2024-01-04T09:47:12.667671\",\n" +
                                "                    \"nextConfidenceCheckAt\": \"2024-01-04T09:47:12.667671\",\n" +
                                "                    \"confidenceLevel\": 0\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"site\": {\n" +
                                "                \"siteBpn\": \"BPNS0000000001YN\",\n" +
                                "                \"name\": null,\n" +
                                "                \"confidenceCriteria\": {\n" +
                                "                    \"sharedByOwner\": false,\n" +
                                "                    \"checkedByExternalDataSource\": false,\n" +
                                "                    \"numberOfBusinessPartners\": 1,\n" +
                                "                    \"lastConfidenceCheckAt\": \"2024-01-05T09:26:02.009513\",\n" +
                                "                    \"nextConfidenceCheckAt\": \"2024-01-05T09:26:02.009513\",\n" +
                                "                    \"confidenceLevel\": 0\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"address\": {\n" +
                                "                \"addressBpn\": \"BPNA000000006X3N\",\n" +
                                "                \"name\": null,\n" +
                                "                \"addressType\": \"LegalAndSiteMainAddress\",\n" +
                                "                \"physicalPostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": null,\n" +
                                "                    \"country\": \"DE\",\n" +
                                "                    \"administrativeAreaLevel1\": null,\n" +
                                "                    \"administrativeAreaLevel2\": null,\n" +
                                "                    \"administrativeAreaLevel3\": null,\n" +
                                "                    \"postalCode\": \"70546\",\n" +
                                "                    \"city\": \"Stuttgart\",\n" +
                                "                    \"district\": null,\n" +
                                "                    \"street\": {\n" +
                                "                        \"namePrefix\": null,\n" +
                                "                        \"additionalNamePrefix\": null,\n" +
                                "                        \"name\": \"Stuttgarter Strasse\",\n" +
                                "                        \"nameSuffix\": null,\n" +
                                "                        \"additionalNameSuffix\": null,\n" +
                                "                        \"houseNumber\": \"1\",\n" +
                                "                        \"houseNumberSupplement\": null,\n" +
                                "                        \"milestone\": null,\n" +
                                "                        \"direction\": null\n" +
                                "                    },\n" +
                                "                    \"companyPostalCode\": null,\n" +
                                "                    \"industrialZone\": null,\n" +
                                "                    \"building\": null,\n" +
                                "                    \"floor\": null,\n" +
                                "                    \"door\": null\n" +
                                "                },\n" +
                                "                \"alternativePostalAddress\": {\n" +
                                "                    \"geographicCoordinates\": null,\n" +
                                "                    \"country\": null,\n" +
                                "                    \"administrativeAreaLevel1\": null,\n" +
                                "                    \"postalCode\": null,\n" +
                                "                    \"city\": null,\n" +
                                "                    \"deliveryServiceType\": null,\n" +
                                "                    \"deliveryServiceQualifier\": null,\n" +
                                "                    \"deliveryServiceNumber\": null\n" +
                                "                },\n" +
                                "                \"confidenceCriteria\": {\n" +
                                "                    \"sharedByOwner\": false,\n" +
                                "                    \"checkedByExternalDataSource\": false,\n" +
                                "                    \"numberOfBusinessPartners\": 1,\n" +
                                "                    \"lastConfidenceCheckAt\": \"2024-01-04T09:47:12.667671\",\n" +
                                "                    \"nextConfidenceCheckAt\": \"2024-01-04T09:47:12.667671\",\n" +
                                "                    \"confidenceLevel\": 0\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"createdAt\": \"2023-11-14T18:17:00.030818Z\",\n" +
                                "            \"updatedAt\": \"2023-11-14T18:17:00.030823Z\"\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}\n")));
    }


}
