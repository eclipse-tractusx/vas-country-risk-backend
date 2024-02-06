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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateBusinessPartnerOutputDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolAddressDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolLegalEntityDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolSiteDto;

import java.util.List;

public class JsonMappingUtils {

    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Ignore unknown properties to prevent errors when JSON has fields not present in DTO
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static List<GateBusinessPartnerOutputDto> mapContentToListOfBusinessPartnerOutputDto(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode contentNode = rootNode.path("content");
            return objectMapper.readValue(contentNode.toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to List<BusinessPartnerOutputDto>", e);
        }
    }
    public static List<PoolLegalEntityDto> mapToListOfPoolLegalEntityDto(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to List<PoolLegalEntityDto>", e);
        }
    }

    public static List<PoolSiteDto> mapJsonToListOfPoolSiteDto(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode contentNode = rootNode.path("content");
            return objectMapper.readValue(contentNode.toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to List<PoolSiteDto>", e);
        }
    }

    public static List<PoolAddressDto> mapJsonToListOfPoolAddressDto(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode contentNode = rootNode.path("content");
            return objectMapper.readValue(contentNode.toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to List<PoolAddressDto>", e);
        }
    }
}
