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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.config.EdcProperties;
import org.eclipse.tractusx.valueaddedservice.dto.edc.CatalogItemDTO;
import org.eclipse.tractusx.valueaddedservice.dto.edc.NegotiationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class EdcLogicService {

    @Autowired
    private InvokeService invokeService;

    @Value("${application.bpdm.consumerManagementUrl}")
    private String consumerManagementUrl;

    @Value("${application.bpdm.gateProviderProtocolUrl}")
    private String gateProviderProtocolUrl;

    @Value("${application.bpdm.apiKey}")
    private String apiKey;

    @Autowired
    private EdcProperties edcProperties;

    @Autowired
    ObjectMapper objectMapper;



    public Mono<String> sendFinalRequest(NegotiationResponseDTO edrResponse, Object body)  {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", edrResponse.getAuthCode());
        return executePostRequest(edrResponse.getEndpoint(), body, headers, response -> response);
    }


    public List<CatalogItemDTO> queryCatalog() {
        HttpHeaders headers = createHttpHeaders();
        Map<String, Object> requestBody = createCatalogRequestBody();
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        log.debug("Sending POST request to URL: " + consumerManagementUrl + "/v2/catalog/request/");
        log.debug("Request Headers: " + headers);
        log.debug("Request Body: " + requestBody);

        return getMockCatalog();
        //return invokeService.executeRequest("default",consumerManagementUrl + "/v2/catalog/request/", HttpMethod.POST, httpEntity, this::mapResponseFromQueryCatalog).block();
    }

    // Helper methods
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


    private List<CatalogItemDTO> mapResponseFromQueryCatalog(String response) {
        List<CatalogItemDTO> catalogItems = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode responseJson = objectMapper.readTree(response);
            JsonNode datasets = responseJson.path("dcat:dataset");

            if (datasets.isArray()) {
                datasets.forEach(dataset -> {
                    String type = dataset.path("dct:type").asText().replace("cx-taxo:", "");
                    if (edcProperties.getProviders().contains(type)) {
                        catalogItems.add(processDatasetAndCreateDTO(dataset));
                    }
                });
            } else if (!datasets.isMissingNode()) {
                String type = datasets.path("dct:type").asText().replace("cx-taxo:", "");
                if (edcProperties.getProviders().contains(type)) {
                    catalogItems.add(processDatasetAndCreateDTO(datasets));
                }
            }
        } catch (IOException e) {
            log.error("Error parsing response JSON: {}", e.getMessage());
        }

        return catalogItems;
    }

    private CatalogItemDTO processDatasetAndCreateDTO(JsonNode dataset) {
        String id = dataset.get("@id").asText();
        String offerId = dataset.path("odrl:hasPolicy").get("@id").asText();
        String subject = dataset.path("dct:subject").asText().replace("cx-taxo:","");
        String description = dataset.path("dct:description").asText();
        String provider = dataset.path("dct:type").asText().replace("cx-taxo:","");

        return new CatalogItemDTO(id, offerId, provider, subject, description);
    }



    public List<CatalogItemDTO> getMockCatalog() {
        String json = "[\n" +
                "    {\n" +
                "        \"id\": \"5191c813-97c7-4a50-8acc-5ad500772640\",\n" +
                "        \"offerId\": \"offer123\",\n" +
                "        \"provider\": \"BPDMGate\",\n" +
                "        \"subject\": \"cx-taxo:ReadAccessPoolForCatenaXMember\",\n" +
                "        \"description\": \"Grants the Catena-X Member read access to the Pool API, allowing for efficient data sharing and management within the BPDM Gate.\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"5191c813-97c7-4a50-8acc-5ad500772642\",\n" +
                "        \"offerId\": \"offer124\",\n" +
                "        \"provider\": \"BPDMPool\",\n" +
                "        \"subject\": \"cx-taxo:WriteAccessPoolForCatenaXMember\",\n" +
                "        \"description\": \"Enables the Catena-X Member write access to the Pool API, facilitating active participation and contribution to the BPDMPool ecosystem.\"\n" +
                "    }\n" +
                "]\n";

        ObjectMapper objectMapper = new ObjectMapper();
        List<CatalogItemDTO> businessPartnerDTOList = new ArrayList<>();
        try {
            businessPartnerDTOList = objectMapper.readValue(json, new TypeReference<List<CatalogItemDTO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessPartnerDTOList;
    }


}
