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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CountryDTO;
import org.eclipse.tractusx.valueaddedservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.eclipse.tractusx.valueaddedservice.constants.VasConstants.CSV_ROLE_READ_CUSTOMER;
import static org.eclipse.tractusx.valueaddedservice.constants.VasConstants.CSV_ROLE_READ_SUPPLIER;

@Service
@Slf4j
public class BusinessPartnersLogicService {

    @Autowired
    ObjectMapper objectMapper;

    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    @Value("${application.partnersPoolUrl.bpdmAddressUrl}")
    private String bpdmAddressUrl;

    @Value("${application.partnersPoolUrl.bpdmLegalUrl}")
    private String bpdmLegalUrl;

    @Value("${application.partnersPoolUrl.bpdmSiteUrl}")
    private String bpdmSiteUrl;

    @Autowired
    InvokeService invokeService;

    @Autowired
    CountryService countryService;



    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUser, String token, List<String> roles) {
        log.debug("getExternalBusinessPartners for companyUserDTO {}", companyUser);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = "[]";
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);

        // Create a map to hold the externalId to legalName mappings
        Map<String, String> externalIdToLegalName = new HashMap<>();

        // Call the first endpoint and create the DTOs
        List<BusinessPartnerDTO> businessPartnerDTOS = invokeService.executeRequest(bpdmLegalUrl, HttpMethod.POST, httpEntity, BusinessPartnerDTO.class, json -> customMappingLogic(json, externalIdToLegalName)).block();

        // Call the third endpoint and create the DTOs, but only if the externalId is not already present in the existing DTOs
        List<BusinessPartnerDTO>  businessPartnerDTOS2 = invokeService.executeRequest(bpdmSiteUrl, HttpMethod.POST, httpEntity, BusinessPartnerDTO.class, json -> customMappingLogic(json, externalIdToLegalName)).block();
        Objects.requireNonNull(businessPartnerDTOS2).removeIf(dto -> isExternalIdPresent(dto.getBpn(), businessPartnerDTOS));
        Objects.requireNonNull(businessPartnerDTOS).addAll(businessPartnerDTOS2);

        // Call the second endpoint and create the DTOs, but only if the externalId is not already present in the existing DTOs
        businessPartnerDTOS2 = invokeService.executeRequest(bpdmAddressUrl, HttpMethod.POST, httpEntity, BusinessPartnerDTO.class, json -> customMappingLogic(json, externalIdToLegalName)).block();
        Objects.requireNonNull(businessPartnerDTOS2).removeIf(dto -> isExternalIdPresent(dto.getBpn(), businessPartnerDTOS));
        Objects.requireNonNull(businessPartnerDTOS).addAll(businessPartnerDTOS2);
        businessPartnerDTOS2.clear();


        // Update country names
        updateCountryNames(businessPartnerDTOS);


        // Filter based on roles
        return filterBusinessPartnersByRole(businessPartnerDTOS, roles);
    }

    private void updateCountryNames(List<BusinessPartnerDTO> businessPartnerDTOS) {
        businessPartnerDTOS.forEach(businessPartnerDTO -> {
            Optional<CountryDTO> countryDTO = countryService.findCountryByIso2(businessPartnerDTO.getCountry());
            if(countryDTO.isPresent()){
                businessPartnerDTO.setCountry(countryDTO.get().getCountry());
            }
        });
    }


    private List<BusinessPartnerDTO> customMappingLogic(String json, Map<String, String> externalIdToLegalName) {
        // Parse the JSON into a JsonNode object
        JsonNode rootNode = parseJson(json);

        // Get the "content" array
        JsonNode contentNode = rootNode.get("content");

        // Create a list to hold the DTOs
        List<BusinessPartnerDTO> dtos = new ArrayList<>();

        // Iterate over the elements in the "content" array
        for (JsonNode elementNode : contentNode) {
            // Get the "externalId" field
            String externalId = getExternalId(elementNode, externalIdToLegalName);

            // Check if the "legalAddress" field exists
            if (elementNode.has("legalAddress")) {
                // Handle as the second type of JSON
                handleSecondTypeJson(elementNode, externalId, dtos, externalIdToLegalName);
            } else if (elementNode.has("mainAddress")) {
                // Handle as the third type of JSON
                handleThirdTypeJson(elementNode, externalId, dtos, externalIdToLegalName);
            } else {
                // Handle as the first type of JSON
                handleFirstTypeJson(elementNode, externalId, dtos, externalIdToLegalName);
            }
        }

        return dtos;
    }

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExternalId(JsonNode elementNode, Map<String, String> externalIdToLegalName) {
        String externalId;
        if (elementNode.has("legalNameParts")) {
            externalId = elementNode.get("externalId").asText();
            String legalName = elementNode.get("legalNameParts").get(0).asText();
            externalIdToLegalName.put(externalId, legalName);
        } else {
            externalId = elementNode.get("legalEntityExternalId").asText();
            JsonNode siteExternalIdNode = elementNode.get("siteExternalId");

            // Check for null legalEntityExternalId and non-null siteExternalId
            if (elementNode.get("legalEntityExternalId").isNull() && siteExternalIdNode != null && !siteExternalIdNode.isNull()) {
                String siteExternalId = siteExternalIdNode.asText();

                // If siteExternalId exists in map, get corresponding legalEntityExternalId
                if (externalIdToLegalName.containsKey(siteExternalId)) {
                    externalId = externalIdToLegalName.get(siteExternalId);
                }
            } else if (siteExternalIdNode != null && !siteExternalIdNode.isNull()) {
                // Handle non-null siteExternalId
                String siteExternalId = siteExternalIdNode.asText();
                externalIdToLegalName.put(siteExternalId, externalId);
            }
        }
        return externalId;
    }


    private void handleSecondTypeJson(JsonNode elementNode, String externalId, List<BusinessPartnerDTO> dtos, Map<String, String> externalIdToLegalName) {
        JsonNode legalAddressNode = elementNode.get("legalAddress");
        String bpna = legalAddressNode.get("bpna").asText();
        if (bpna != null) {
            dtos.add(createDto(bpna, legalAddressNode, externalIdToLegalName.get(externalId)));
        }
        String bpnl = elementNode.get("bpnl").asText();
        if (bpnl != null) {
            dtos.add(createDto(bpnl, legalAddressNode, externalIdToLegalName.get(externalId)));
        }
    }

    private void handleFirstTypeJson(JsonNode elementNode, String externalId, List<BusinessPartnerDTO> dtos, Map<String, String> externalIdToLegalName) {
        String bpna = elementNode.get("bpna").asText();
        if (bpna != null) {
            dtos.add(createDto(bpna, elementNode, externalIdToLegalName.get(externalId)));
        }
    }

    private void handleThirdTypeJson(JsonNode elementNode, String externalId, List<BusinessPartnerDTO> dtos, Map<String, String> externalIdToLegalName) {
        JsonNode mainAddressNode = elementNode.get("mainAddress");
        String bpna = mainAddressNode.get("bpna").asText();
        if (bpna != null) {
            dtos.add(createDto(bpna, mainAddressNode, externalIdToLegalName.get(externalId)));
        }
        String bpns = elementNode.get("bpns").asText();
        if (bpns != null) {
            dtos.add(createDto(bpns, mainAddressNode, externalIdToLegalName.get(externalId)));
        }
    }


    private BusinessPartnerDTO createDto(String bpn, JsonNode addressNode, String legalName) {
        BusinessPartnerDTO dto = new BusinessPartnerDTO();
        dto.setBpn(bpn);
        dto.setLegalName(legalName);
        setFieldsFromAddress(dto, addressNode);
        setRoles(dto, addressNode);
        return dto;
    }

    private void setFieldsFromAddress(BusinessPartnerDTO dto, JsonNode addressNode) {
        JsonNode physicalPostalAddressNode = addressNode.get("physicalPostalAddress");
        if (physicalPostalAddressNode != null) {
            JsonNode streetNode = physicalPostalAddressNode.get("street");
            if (streetNode != null) {
                JsonNode nameNode = streetNode.get("name");
                if (nameNode != null) {
                    dto.setStreet(nameNode.asText());
                }

                JsonNode houseNumberNode = streetNode.get("houseNumber");
                if (houseNumberNode != null) {
                    dto.setHouseNumber(houseNumberNode.asText());
                }
            }

            JsonNode postalCodeNode = physicalPostalAddressNode.get("postalCode");
            if (postalCodeNode != null) {
                dto.setZipCode(postalCodeNode.asText());
            }

            JsonNode cityNode = physicalPostalAddressNode.get("city");
            if (cityNode != null) {
                dto.setCity(cityNode.asText());
            }

            JsonNode countryNode = physicalPostalAddressNode.get("country");
            if (countryNode != null) {
                dto.setCountry(countryNode.asText());
            }

            JsonNode geographicCoordinatesNode = physicalPostalAddressNode.get("geographicCoordinates");
            if (geographicCoordinatesNode != null) {
                JsonNode longitudeNode = geographicCoordinatesNode.get("longitude");
                if (longitudeNode != null) {
                    dto.setLongitude(longitudeNode.asText());
                }

                JsonNode latitudeNode = geographicCoordinatesNode.get("latitude");
                if (latitudeNode != null) {
                    dto.setLatitude(latitudeNode.asText());
                }
            }
        }

    }

    private void setRoles(BusinessPartnerDTO dto, JsonNode addressNode) {
        JsonNode rolesNode = addressNode.get("ROLES");
        if (rolesNode != null) {
            for (JsonNode roleNode : rolesNode) {
                String role = roleNode.asText();
                if ("Supplier".equals(role)) {
                    dto.setSupplier(true);
                } else if ("Customer".equals(role)) {
                    dto.setCustomer(true);
                }
            }
        }
    }

    private boolean isExternalIdPresent(String externalId, List<BusinessPartnerDTO> dtos) {
        return dtos.stream().anyMatch(dto -> dto.getBpn().equals(externalId));
    }

    private List<BusinessPartnerDTO> filterBusinessPartnersByRole(List<BusinessPartnerDTO> businessPartnerDTOS, List<String> roles) {
        List<BusinessPartnerDTO> filteredBusinessPartnerDTOS;

        if (roles.contains(CSV_ROLE_READ_SUPPLIER) && roles.contains(CSV_ROLE_READ_CUSTOMER)) {
            // User has both roles, no need to filter
            filteredBusinessPartnerDTOS = businessPartnerDTOS;
        } else if (roles.contains(CSV_ROLE_READ_SUPPLIER)) {
            // User can only read suppliers and those who are not customers
            filteredBusinessPartnerDTOS = filterSuppliers(businessPartnerDTOS);
        } else if (roles.contains(CSV_ROLE_READ_CUSTOMER)) {
            // User can only read customers and those who are not suppliers
            filteredBusinessPartnerDTOS = filterCustomers(businessPartnerDTOS);
        } else {
            // User has neither role, can only see those who are neither suppliers nor customers
            filteredBusinessPartnerDTOS = filterNeither(businessPartnerDTOS);
        }
        return filteredBusinessPartnerDTOS;
    }

    private List<BusinessPartnerDTO> filterSuppliers(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> bpn.getSupplier() || !bpn.getCustomer())
                .peek(bpn -> bpn.setCustomer(false))
                .toList();
    }

    private List<BusinessPartnerDTO> filterCustomers(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> bpn.getCustomer() || !bpn.getSupplier())
                .peek(bpn -> bpn.setSupplier(false))
                .toList();
    }

    private List<BusinessPartnerDTO> filterNeither(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> !bpn.getSupplier() && !bpn.getCustomer())
                .toList();
    }
}
