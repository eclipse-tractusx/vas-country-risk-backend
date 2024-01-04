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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.edc.EDRResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class EDCLogicService {

    @Autowired
    private InvokeService invokeService;

    @Value("${application.bpdm.consumerManagementUrl}")
    private String consumerManagementUrl;

    @Value("${application.bpdm.gateProviderProtocolUrl}")
    private String gateProviderProtocolUrl;

    @Value("${application.bpdm.gateProviderId}")
    private String gateProviderId;

    @Value("${application.bpdm.policyBpn}")
    private String policyBpn;

    @Value("${application.bpdm.apiKey}")
    private String apiKey;

    @Autowired
    ObjectMapper objectMapper;

    public Mono<String> executeSequentialRequests(String assetId,Object body) {
        Map<String, String> response = queryCatalog();
        String offerId = response.get("ASSET_" + assetId);

        if (offerId == null) {
            log.error("Offer ID is missing");
            return Mono.error(new RuntimeException("Asset ID or Offer ID is missing"));
        }

        return retrieveEDRsData(assetId)
                .flatMap(lastNegotiatedTransferProcessId -> {
                    if (lastNegotiatedTransferProcessId.isEmpty()) {
                        log.info("No negotiated transfer process ID found");
                        log.info("Initiating Negotiation");
                        return sendNegotiationInitiateRequest(offerId, assetId)
                                .then(Mono.delay(Duration.ofSeconds(3))) // Non-blocking delay
                                .then(retrieveEDRsData(assetId))
                                .flatMap(this::getEDRById)
                                .flatMap(responseFromEDRById -> sendFinalRequest(responseFromEDRById, body));
                    } else {
                        log.debug("Found negotiated transfer process ID");
                        return getEDRById(lastNegotiatedTransferProcessId)
                                .flatMap(responseFromEDRById -> sendFinalRequest(responseFromEDRById, body));
                    }
                });
    }

    public Mono<EDRResponse> getEDRById(String transferProcessId) {
        return executeGetRequest(consumerManagementUrl + "/edrs/" + transferProcessId, this::parseEDRResponse);
    }

    public Mono<String> sendFinalRequest(EDRResponse edrResponse, Object body)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", edrResponse.getAuthCode());
        return executePostRequest(edrResponse.getEndpoint(), body, headers, response -> response);
    }


    public Mono<String> retrieveEDRsData(String assetId) {
        return executeGetRequest(consumerManagementUrl + "/edrs?assetId=" + assetId, this::extractLastNegotiatedTransferProcessId);
    }

    public Map<String, String> queryCatalog() {
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = createCatalogRequestBody();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        log.debug("Sending POST request to URL: " + consumerManagementUrl + "/v2/catalog/request/");
        log.debug("Request Headers: " + headers.toString());
        log.debug("Request Body: " + requestBody.toString());

        return invokeService.executeRequest("default",consumerManagementUrl + "/v2/catalog/request/", HttpMethod.POST, httpEntity, this::mapResponseFromQueryCatalog).block();
    }

    public Mono<Boolean> sendNegotiationInitiateRequest(String offerId, String assetId) {
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = createNegotiationRequestBody(offerId, assetId);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        // Log the URL, Headers, and Body
        String url = consumerManagementUrl + "/edrs";
        log.debug("Sending POST request to URL: " + url);
        log.debug("Request Headers: " + headers.toString());
        log.debug("Request Body: " + requestBody.toString());

        return invokeService.executeRequest("default",consumerManagementUrl + "/edrs", HttpMethod.POST, httpEntity);
    }

    // Helper methods

    private <T> Mono<T> executeGetRequest(String url, Function<String, T> responseMapper) {
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(new HashMap<>(), headers);

        return invokeService.executeRequest("default",url, HttpMethod.GET, httpEntity, responseMapper);
    }

    private <T> Mono<T> executePostRequest(String url, Object body, HttpHeaders headers, Function<String, T> responseMapper) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
        return invokeService.executeRequest("default",url, HttpMethod.POST, httpEntity, responseMapper);
    }


    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", apiKey);
        return headers;
    }

    private Map<String, Object> createCatalogRequestBody() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("@context", new HashMap<>());
        requestBody.put("protocol", "dataspace-protocol-http");
        requestBody.put("providerUrl", gateProviderProtocolUrl);

        Map<String, Object> querySpec = new HashMap<>();
        querySpec.put("offset", 0);
        querySpec.put("limit", 100);
        querySpec.put("filter", "");

        Map<String, Integer> range = new HashMap<>();
        range.put("from", 0);
        range.put("to", 100);
        querySpec.put("range", range);
        querySpec.put("criterion", "");

        requestBody.put("querySpec", querySpec);
        return requestBody;
    }

    private Map<String, Object> createNegotiationRequestBody(String offerId, String assetId) {
        Map<String, Object> body = new HashMap<>();
        body.put("@context", Collections.singletonMap("odrl", "http://www.w3.org/ns/odrl/2/"));
        body.put("@type", "NegotiationInitiateRequestDto");
        body.put("connectorAddress", gateProviderProtocolUrl);
        body.put("protocol", "dataspace-protocol-http");
        body.put("connectorId", gateProviderId);
        body.put("providerId", gateProviderId);

        Map<String, Object> offer = new HashMap<>();
        offer.put("offerId", offerId);
        offer.put("assetId", assetId);

        Map<String, Object> policy = new HashMap<>();
        policy.put("@type", "odrl:Set");

        Map<String, Object> permission = new HashMap<>();
        permission.put("odrl:target", assetId);
        permission.put("odrl:action", Collections.singletonMap("odrl:type", "USE"));

        Map<String, Object> constraint = new HashMap<>();
        Map<String, Object> orConstraint = new HashMap<>();
        orConstraint.put("odrl:leftOperand", "BusinessPartnerNumber");
        orConstraint.put("odrl:operator", Collections.singletonMap("@id", "odrl:eq"));
        orConstraint.put("odrl:rightOperand", policyBpn);
        constraint.put("odrl:or", orConstraint);

        permission.put("odrl:constraint", constraint);
        policy.put("odrl:permission", permission);

        policy.put("odrl:prohibition", Collections.emptyList());
        policy.put("odrl:obligation", Collections.emptyList());
        policy.put("odrl:target",assetId);

        offer.put("policy", policy);
        body.put("offer", offer);
        return body;
    }

    private EDRResponse parseEDRResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            EDRResponse response = new EDRResponse();
            if (rootNode.has("edc:authCode")) {
                response.setAuthCode(rootNode.get("edc:authCode").asText());
            }
            if (rootNode.has("edc:endpoint")) {
                response.setEndpoint(rootNode.get("edc:endpoint").asText());
            }
            return response;
        } catch (IOException e) {
            log.error("Error parsing JSON: {}", e.getMessage());
            throw new RuntimeException("Error Getting authCode or Endpoint");
        }
    }

    private Map<String, String> mapResponseFromQueryCatalog(String response) {
        Map<String, String> assetOfferMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode responseJson = objectMapper.readTree(response);
            JsonNode datasets = responseJson.path("dcat:dataset");

            if (datasets.isArray()) {
                for (JsonNode dataset : datasets) {
                    processDatasetAndAddToMap(dataset, assetOfferMap);
                }
            } else if (!datasets.isMissingNode()) {
                processDatasetAndAddToMap(datasets, assetOfferMap);
            }
        } catch (IOException e) {
            log.error("Error parsing response JSON: {}", e.getMessage());
        }

        return assetOfferMap;
    }

    private String extractLastNegotiatedTransferProcessId(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String lastNegotiatedTransferProcessId = "";

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    if (node.has("tx:edrState") && "NEGOTIATED".equals(node.get("tx:edrState").asText())) {
                        if (node.has("edc:transferProcessId")) {
                            lastNegotiatedTransferProcessId = node.get("edc:transferProcessId").asText();
                        }
                    }
                }
            } else {
                log.info("Response is not an array.");
            }
        } catch (IOException e) {
            log.error("Error parsing JSON: {}", e.getMessage());
        }

        if (lastNegotiatedTransferProcessId.isEmpty()) {
            log.info("No negotiated transfer process ID found.");
        }

        return lastNegotiatedTransferProcessId;
    }


    private void processDatasetAndAddToMap(JsonNode dataset, Map<String, String> map) {
        if (dataset.has("@id") && dataset.has("odrl:hasPolicy") && dataset.get("odrl:hasPolicy").has("@id")) {
            String id = dataset.get("@id").asText();
            String offerId = dataset.get("odrl:hasPolicy").get("@id").asText();
            map.put("ASSET_" + id, offerId);
        }
    }

}
