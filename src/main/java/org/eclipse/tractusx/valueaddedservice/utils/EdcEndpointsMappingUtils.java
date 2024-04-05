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
package org.eclipse.tractusx.valueaddedservice.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.edc.EDRResponseDTO;

import java.io.IOException;

@Slf4j
public class EdcEndpointsMappingUtils {

    public EdcEndpointsMappingUtils() {
    }

    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static String extractLastNegotiatedTransferProcessId(String jsonResponse) {
        String lastNegotiatedTransferProcessId = "";
        long latestExpirationDate = 0;

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    if ("NEGOTIATED".equals(node.path("tx:edrState").asText()) && node.has("tx:expirationDate")) {
                        long currentExpirationDate = node.path("tx:expirationDate").asLong();
                        // Check if this entry is more recent based on expirationDate
                        if (currentExpirationDate > latestExpirationDate) {
                            latestExpirationDate = currentExpirationDate;
                            if (node.has("transferProcessId")) {
                                lastNegotiatedTransferProcessId = node.path("transferProcessId").asText();
                            }
                        }
                    }
                }
            } else {
                log.info("Expected an array response for EDRs data but got a non-array response.");
            }
        } catch (IOException e) {
            log.error("Error parsing JSON for the last negotiated transfer process ID: {}", e.getMessage());
        }

        if (lastNegotiatedTransferProcessId.isEmpty()) {
            log.info("No negotiated transfer process ID found in the latest entry.");
        }
        return lastNegotiatedTransferProcessId;
    }

    public static String extractNegotiationIdFromInitialRequest(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String negotiationId = "";
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            negotiationId = rootNode.get("@id").asText();
        } catch (IOException e) {
            log.error("Error parsing negotiation ID from JSON: {}", e.getMessage());
            throw new RuntimeException("Error extracting negotiation ID");
        }
        isEmpty(negotiationId,"extractNegotiationIdFromInitialRequest",jsonResponse);
        return negotiationId;
    }

    public static String extractContractAgreementId(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String contractAgreementId = "";
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            contractAgreementId = rootNode.path("contractAgreementId").asText("");
        } catch (IOException e) {
            log.error("Error parsing contract agreement ID from JSON: {}", e.getMessage());
            throw new RuntimeException("Error extracting contract agreement ID");
        }
        isEmpty(contractAgreementId,"extractContractAgreementId",jsonResponse);
        return contractAgreementId;
    }

    public static String extractTransferProcessId(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        String transferProcessId = "";
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.isArray() && !rootNode.isEmpty()) {
                // Assuming you want the transferProcessId from the first object in the array
                JsonNode firstItem = rootNode.get(0);
                if (firstItem != null && firstItem.has("transferProcessId")) {
                    transferProcessId =  firstItem.get("transferProcessId").asText("");
                }
            }
        } catch (IOException e) {
            log.error("Error parsing transfer process ID from JSON: {}", e.getMessage());
            throw new RuntimeException("Error extracting transfer process ID");
        }

        isEmpty(transferProcessId,"extractTransferProcessId",jsonResponse);
        return transferProcessId;
    }


    public static EDRResponseDTO getAuthCodeAndEndpoint(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            EDRResponseDTO response = new EDRResponseDTO();
            if (rootNode.has("authCode")) {
                response.setAuthCode(rootNode.get("authCode").asText());
            }
            if (rootNode.has("endpoint")) {
                response.setEndpoint(rootNode.get("endpoint").asText());
            }
            return response;
        } catch (IOException e) {
            log.error("Error parsing JSON: {}", e.getMessage());
            throw new RuntimeException("Error Getting authCode or Endpoint");
        }

    }

    private static void isEmpty(String extractMessage, String operation, String json){
        if(extractMessage.isEmpty()){
            log.error("Error Extracting from Operation: {}", operation);
            log.error("Error Extracting from JSON: {}", json);
            throw new RuntimeException(String.format("Error in operation: %s", operation));
        }
        log.info("Found {} for Operation {}",extractMessage,operation);
    }

}

