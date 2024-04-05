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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.edc.EDRResponseDTO;
import org.eclipse.tractusx.valueaddedservice.dto.edc.NegotiationRequestDTO;
import org.eclipse.tractusx.valueaddedservice.dto.edc.NegotiationResponseDTO;
import org.eclipse.tractusx.valueaddedservice.utils.EdcEndpointsMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
@Slf4j
public class NegotiationServiceLogic {


    @Autowired
    private InvokeService invokeService;

    @Value("${application.bpdm.gateProviderProtocolUrl}")
    private String gateProviderProtocolUrl;

    @Value("${application.bpdm.consumerManagementUrl}")
    private String consumerManagementUrl;

    @Value("${application.bpdm.policyBpn}")
    private String policyBpn;

    @Value("${application.bpdm.apiKey}")
    private String apiKey;

    @Value("${application.bpdm.gateProviderId}")
    private String gateProviderId;

    private final ConcurrentHashMap<String, NegotiationResponseDTO> negotiationCache = new ConcurrentHashMap<>();


    @Cacheable(value = "vas-bpdm-negotiation", key = "{#root.methodName, #negotiationItems}", unless = "#result == null or #result.isEmpty()")
    public List<NegotiationResponseDTO> triggerNegotiation(List<NegotiationRequestDTO> negotiationItems) {
        log.info("Triggering negotiation for items: {}", negotiationItems);

        List<NegotiationResponseDTO> responses = Flux.fromIterable(negotiationItems)
                .flatMap(dto ->
                        executeSequentialNegotiationRequests(dto.getId(), dto.getOfferId())
                                .map(response -> new NegotiationResponseDTO(dto.getId(), dto.getOfferId(), gateProviderProtocolUrl, "Success", response.getAuthCode(), response.getEndpoint()))
                                .onErrorResume(e -> Mono.just(new NegotiationResponseDTO(dto.getId(), dto.getOfferId(), gateProviderProtocolUrl, "Error", null, null)))
                ).collectList().block();

        responses.stream().forEach(dto -> negotiationCache.put(dto.getId(), dto));

        return responses;

    }

    public ConcurrentHashMap<String, NegotiationResponseDTO> getStoredNegotiation() {
        return negotiationCache;
    }

    public Mono<EDRResponseDTO> executeSequentialNegotiationRequests(String assetId, String offerId) {

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
                                .delayElement(Duration.ofSeconds(3))
                                .flatMap(this::executeGetRequestForNegotiationDetails) // Returns contractAgreementId
                                .delayElement(Duration.ofSeconds(3))
                                .flatMap(this::executeGetRequestWithAgreementId) // Returns transferProcessId
                                .delayElement(Duration.ofSeconds(3))
                                .flatMap(this::getAuthCodeAndEndpoint); // Returns authCode and endpoint

                    } else {
                        log.debug("Found negotiated transfer process ID");
                        return getAuthCodeAndEndpoint(lastNegotiatedTransferProcessId);
                    }
                });
    }


    public Mono<String> sendNegotiationInitiateRequest(String offerId, String assetId) {
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = createNegotiationRequestBody(offerId, assetId);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        // Log the URL, Headers, and Body
        String url = consumerManagementUrl + "/edrs";
        log.debug("Sending POST request to URL: " + url);
        log.debug("Request Headers: " + headers.toString());
        log.debug("Request Body: " + requestBody.toString());

        return invokeService.executeRequest("default", url, HttpMethod.POST, httpEntity, EdcEndpointsMappingUtils::extractNegotiationIdFromInitialRequest);
    }

    public Mono<EDRResponseDTO> getAuthCodeAndEndpoint(String transferProcessId) {
        return executeGetRequest(consumerManagementUrl + "/edrs/" + transferProcessId, EdcEndpointsMappingUtils::getAuthCodeAndEndpoint);
    }

    @CacheEvict(value = "vas-bpdm-negotiation", allEntries = true)
    public void invalidateAllCache() {
        negotiationCache.clear();
        log.debug("invalidateAllCache|vas-bpdm-negotiation -  invalidated cache - allEntries");
    }

    public Mono<String> executeGetRequestForNegotiationDetails(String negotiationId) {
        String url = consumerManagementUrl + "/v2/contractnegotiations/" + negotiationId;
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = new HashMap<>();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        return invokeService.executeRequest("default", url, HttpMethod.GET, httpEntity, EdcEndpointsMappingUtils::extractContractAgreementId)
                .doOnError(error -> log.error("Failed to retrieve contract negotiation details", error));
    }

    public Mono<String> executeGetRequestWithAgreementId(String contractAgreementId) {
        String url = consumerManagementUrl + "/edrs?agreementId=" + contractAgreementId;
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = new HashMap<>();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        return invokeService.executeRequest("default", url, HttpMethod.GET, httpEntity, EdcEndpointsMappingUtils::extractTransferProcessId)
                .doOnError(error -> log.error("Failed to make request with agreement ID: {}", contractAgreementId, error));
    }


    public Map<String, Object> createNegotiationRequestBody(String offerId, String assetId) {
        Map<String, Object> body = new HashMap<>();
        body.put("@context", Collections.singletonMap("odrl", "http://www.w3.org/ns/odrl/2/"));
        body.put("@type", "NegotiationInitiateRequestDto");
        body.put("counterPartyAddress", gateProviderProtocolUrl);
        body.put("protocol", "dataspace-protocol-http");
        body.put("counterPartyId", gateProviderId);
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
        orConstraint.put("odrl:rightOperand", policyBpn); // Use the specific business partner number here
        constraint.put("odrl:or", orConstraint);

        permission.put("odrl:constraint", constraint);
        policy.put("odrl:permission", permission);

        policy.put("odrl:prohibition", Collections.emptyList());
        policy.put("odrl:obligation", Collections.emptyList());
        policy.put("odrl:target", assetId);

        offer.put("policy", policy);
        body.put("offer", offer);
        return body;
    }

    public Mono<String> retrieveEDRsData(String assetId) {

        return executeGetRequest(consumerManagementUrl + "/edrs?assetId=" + assetId, EdcEndpointsMappingUtils::extractLastNegotiatedTransferProcessId);
    }


    private <T> Mono<T> executeGetRequest(String url, Function<String, T> responseMapper) {
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(new HashMap<>(), headers);

        return invokeService.executeRequest("default", url, HttpMethod.GET, httpEntity, responseMapper);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Api-Key", apiKey);
        return headers;
    }


}
