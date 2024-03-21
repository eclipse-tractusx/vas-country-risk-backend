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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.AddressType;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.BusinessPartnerRole;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateBusinessPartnerOutputDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolAddressDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolLegalEntityDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolSiteDto;
import org.eclipse.tractusx.valueaddedservice.utils.BpdmEndpointsMappingUtils;
import org.eclipse.tractusx.valueaddedservice.utils.JsonMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class RequestLogicService {


    @Autowired
    EDCLogicService edcLogicService;

    @Value("${application.edc.enabled:}")
    private boolean sequentialRequestsEnabled;

    @Value("${application.bpdm.addressUrl}")
    private String bpdmAddressUrl;

    @Value("${application.bpdm.legalUrl}")
    private String bpdmLegalUrl;

    @Value("${application.bpdm.siteUrl}")
    private String bpdmSiteUrl;

    @Value("${application.bpdm.genericUrl}")
    private String bpdmGenericUrl;

    @Autowired
    InvokeService invokeService;


    @Cacheable(value = "vas-bpdm", key = "{#root.methodName , {#roles}}", unless = "#result == null")
    public List<BusinessPartnerDTO> handleRequestsToBpdm(List<String> roles) {
        List<BusinessPartnerDTO> finalDtoList = new ArrayList<>();
//        if (sequentialRequestsEnabled) {
//            finalDtoList.addAll(handleSequentialRequests());
//        } else {
//            finalDtoList.addAll(handleNonSequentialRequests());
//        }
        return getMockBusinessPartnerDTOs();
    }



    private List<BusinessPartnerDTO> handleSequentialRequests() {
        List<BusinessPartnerDTO> finalDtoList = new ArrayList<>();

        log.info("Sequential requests enabled. Starting process to fetch external business partners from generic.");
        String genericEndPointResponse = edcLogicService.executeSequentialRequests("POST_GENERIC_OUTPUT_SEARCH", Collections.emptyList()).block();
        Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> map = processBusinessPartners(JsonMappingUtils.mapContentToListOfBusinessPartnerOutputDto(genericEndPointResponse));
        log.info("Processed business partners from generic endpoint");

        log.info("Starting process to fetch external business partners from legal entity on pool.");
        List<String> bpnlList = getBpnsByAddressType(map, AddressType.LegalAddress);
        String bpnl = edcLogicService.executeSequentialRequests("POST_BPL_POOL_SEARCH", bpnlList).block();
        List<PoolLegalEntityDto> poolLegalEntityDtos = JsonMappingUtils.mapToListOfPoolLegalEntityDto(bpnl);
        log.info("Processed business partners from legal entity on pool, list size {}", poolLegalEntityDtos.size());

        log.info("Starting process to fetch external business partners from site on pool.");
        List<String> bpnsList = getBpnsByAddressType(map, AddressType.SiteMainAddress);
        Map<String, List<String>> sitesBody = new HashMap<>();
        sitesBody.put("sites", bpnsList);
        String bpns = edcLogicService.executeSequentialRequests("POST_BPS_POOL_SEARCH", sitesBody).block();
        List<PoolSiteDto> poolSiteDtoList = JsonMappingUtils.mapJsonToListOfPoolSiteDto(bpns);
        List<String> bpnaList = getBpnsByAddressType(map, AddressType.AdditionalAddress);
        log.info("Processed business partners from site on pool, list size {}", poolSiteDtoList.size());

        log.info("Starting process to fetch external business partners from address on pool.");
        Map<String, List<String>> addressesBody = new HashMap<>();
        addressesBody.put("addresses", bpnaList);
        String bpna = edcLogicService.executeSequentialRequests("POST_BPA_POOL_SEARCH", addressesBody).block();
        List<PoolAddressDto> poolAddressDtos = JsonMappingUtils.mapJsonToListOfPoolAddressDto(bpna);
        log.info("Processed business partners from address on pool, list size {}", poolAddressDtos.size());

        finalDtoList.addAll(poolLegalEntityDtos.stream()
                .map(legalEntity -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(legalEntity, map.get(AddressType.LegalAddress))).toList());
        finalDtoList.addAll(poolSiteDtoList.stream()
                .map(siteDto -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(siteDto, map.get(AddressType.SiteMainAddress))).toList());
        finalDtoList.addAll(poolAddressDtos.stream()
                .map(addressDto -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(addressDto, map.get(AddressType.AdditionalAddress))).toList());

        log.info("Completed mapping of business partners to DTOs.");
        return finalDtoList;
    }

    private List<BusinessPartnerDTO> handleNonSequentialRequests() {
        List<BusinessPartnerDTO> finalDtoList = new ArrayList<>();

        String body = "[]";
        HttpEntity<Object> httpEntity = createHttpEntity(body);

        log.info("Sequential requests not enabled. Starting process to fetch external business partners from generic.");
        List<GateBusinessPartnerOutputDto> gateBusinessPartnerOutputDtos = invokeService.executeRequest("gate", bpdmGenericUrl, HttpMethod.POST, httpEntity, GateBusinessPartnerOutputDto.class, JsonMappingUtils::mapContentToListOfBusinessPartnerOutputDto).block();
        Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> map = processBusinessPartners(gateBusinessPartnerOutputDtos);
        log.info("Processed business partners from generic endpoint");

        log.info("Starting process to fetch external business partners from legal entity on pool.");
        List<String> bpnlList = getBpnsByAddressType(map, AddressType.LegalAddress);
        httpEntity = createHttpEntity(bpnlList);
        List<PoolLegalEntityDto> poolLegalEntityDtos = invokeService.executeRequest("pool", bpdmLegalUrl, HttpMethod.POST, httpEntity, JsonMappingUtils::mapToListOfPoolLegalEntityDto).block();
        log.info("Processed business partners from legal entity on pool, list size {}", poolLegalEntityDtos.size());

        log.info("Starting process to fetch external business partners from site on pool.");
        List<String> bpnsList = getBpnsByAddressType(map, AddressType.SiteMainAddress);
        Map<String, List<String>> sitesBody = new HashMap<>();
        sitesBody.put("sites", bpnsList);
        httpEntity = createHttpEntity(sitesBody);
        List<PoolSiteDto> poolSiteDtos = invokeService.executeRequest("pool", bpdmSiteUrl, HttpMethod.POST, httpEntity, JsonMappingUtils::mapJsonToListOfPoolSiteDto).block();
        log.info("Processed business partners from site on pool, list size {}", poolSiteDtos.size());

        log.info("Starting process to fetch external business partners from address on pool.");
        List<String> bpnaList = getBpnsByAddressType(map, AddressType.AdditionalAddress);
        Map<String, List<String>> addressesBody = new HashMap<>();
        addressesBody.put("addresses", bpnaList);
        httpEntity = createHttpEntity(addressesBody);

        List<PoolAddressDto> poolAddressDtos = invokeService.executeRequest("pool", bpdmAddressUrl, HttpMethod.POST, httpEntity, JsonMappingUtils::mapJsonToListOfPoolAddressDto).block();
        log.info("Processed business partners from address on pool, list size {}", poolAddressDtos.size());

        finalDtoList.addAll(poolLegalEntityDtos.stream()
                .map(legalEntity -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(legalEntity, map.get(AddressType.LegalAddress))).toList());
        finalDtoList.addAll(poolSiteDtos.stream()
                .map(siteDto -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(siteDto, map.get(AddressType.SiteMainAddress))).toList());
        finalDtoList.addAll(poolAddressDtos.stream()
                .map(addressDto -> BpdmEndpointsMappingUtils.mapToBusinessPartnerDto(addressDto, map.get(AddressType.AdditionalAddress))).toList());

        return finalDtoList;
    }

    private HttpEntity<Object> createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
    private Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> processBusinessPartners(List<GateBusinessPartnerOutputDto> list) {
        Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> map = new HashMap<>();
        list.forEach(dto -> Optional.ofNullable(dto.getAddress().getAddressType()).ifPresent(type -> processAddressType(map, type, dto)));
        return map;
    }

    private void processAddressType(Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> map, AddressType addressType, GateBusinessPartnerOutputDto dto) {
        if (EnumSet.of(AddressType.LegalAndSiteMainAddress, AddressType.LegalAddress).contains(addressType)) {
            addToList(map, AddressType.LegalAddress, dto.getRoles(),dto.getLegalEntity().getLegalEntityBpn());
        }
        if (EnumSet.of(AddressType.LegalAndSiteMainAddress, AddressType.SiteMainAddress).contains(addressType)) {
            addToList(map, AddressType.SiteMainAddress, dto.getRoles(),dto.getSite().getSiteBpn());
        }
        addToList(map, AddressType.AdditionalAddress, dto.getRoles(),dto.getAddress().getAddressBpn());
    }

    private void addToList(Map<AddressType, Map<String,Collection<BusinessPartnerRole>>> map, AddressType key, Collection<BusinessPartnerRole>  roles, String value) {
        map.computeIfAbsent(key, k -> new HashMap<>()).put(value,roles);
    }

    public List<String> getBpnsByAddressType(Map<AddressType, Map<String, Collection<BusinessPartnerRole>>> map, AddressType addressType) {
        List<String> bpns = new ArrayList<>();

        if (map.containsKey(addressType)) {
            bpns.addAll(map.get(addressType).keySet());
        }

        return bpns;
    }


    public List<BusinessPartnerDTO> getMockBusinessPartnerDTOs() {
        String json = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"bpn\": \"BPN-0001\",\n" +
                "    \"legalName\": \"Divape Company\",\n" +
                "    \"street\": \"1st Avenue\",\n" +
                "    \"houseNumber\": \"100A\",\n" +
                "    \"zipCode\": \"633104\",\n" +
                "    \"city\": \"Covilhã\",\n" +
                "    \"country\": \"ES\",\n" +
                "    \"longitude\": \"107.6185727\",\n" +
                "    \"latitude\": \"-6.6889038\",\n" +
                "    \"supplier\": false,\n" +
                "    \"customer\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"bpn\": \"BPN-0002\",\n" +
                "    \"legalName\": \"Innovatech Solutions\",\n" +
                "    \"street\": \"Tech Park Rd\",\n" +
                "    \"houseNumber\": \"20B\",\n" +
                "    \"zipCode\": \"500010\",\n" +
                "    \"city\": \"Lisbon\",\n" +
                "    \"country\": \"DE\",\n" +
                "    \"longitude\": \"108.123456\",\n" +
                "    \"latitude\": \"-7.123456\",\n" +
                "    \"supplier\": true,\n" +
                "    \"customer\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"bpn\": \"BPN-0004\",\n" +
                "    \"legalName\": \"Innovatech Solutions Made Up\",\n" +
                "    \"street\": \"Tech Park Rd\",\n" +
                "    \"houseNumber\": \"20B\",\n" +
                "    \"zipCode\": \"500010\",\n" +
                "    \"city\": \"Paris\",\n" +
                "    \"country\": \"FR\",\n" +
                "    \"longitude\": \"108.123456\",\n" +
                "    \"latitude\": \"-7.123456\",\n" +
                "    \"supplier\": false,\n" +
                "    \"customer\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"bpn\": \"BPN-0003\",\n" +
                "    \"legalName\": \"Eco Friendly Packaging\",\n" +
                "    \"street\": \"Greenway Dr\",\n" +
                "    \"houseNumber\": \"5\",\n" +
                "    \"zipCode\": \"755004\",\n" +
                "    \"city\": \"Porto\",\n" +
                "    \"country\": \"PT\",\n" +
                "    \"longitude\": \"106.654321\",\n" +
                "    \"latitude\": \"-5.654321\",\n" +
                "    \"supplier\": true,\n" +
                "    \"customer\": true\n" +
                "  }\n" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<BusinessPartnerDTO> businessPartnerDTOList = new ArrayList<>();
        try {
            businessPartnerDTOList = objectMapper.readValue(json, new TypeReference<List<BusinessPartnerDTO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessPartnerDTOList;
    }




}
