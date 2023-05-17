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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CountryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExternalBusinessPartnersLogicService {

    @Autowired
    ObjectMapper objectMapper;

    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    @Value("${application.partnersPoolUrl.legalEntities}")
    private String legalEntitiesUrl;

    @Autowired
    ApplicationVariables applicationVariables;

    @Autowired
    InvokeService invokeService;

    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#companyUser.name,#companyUser.email,#companyUser.companyName}}", unless = "#result == null")
    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUser) {
        log.debug("getExternalBusinessPartners for companyUserDTO {}",companyUser);
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(applicationVariables.getToken());
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            return objectMapper.readValue(json.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<String> getExternalPartnersCountry (CompanyUserDTO companyUserDTO) {
        log.debug("getExternalPartnersCountry for companyUserDTO {}",companyUserDTO);
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUserDTO);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));
        return countryList;
    }

    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#countryDTO.iso3, #companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public Long getTotalBpnByCountry(CountryDTO countryDTO,CompanyUserDTO companyUserDTO){
        log.debug("getTotalBpnByCountry filtered by country {} and companyUser {}",countryDTO,companyUserDTO);
        List<BusinessPartnerDTO> businessPartnerDTOS = getExternalBusinessPartners(companyUserDTO);
        return  businessPartnerDTOS.stream().filter(businessPartnerDTO -> businessPartnerDTO.getCountry().equalsIgnoreCase(countryDTO.getCountry())).count();

    }

    @CacheEvict(value = "vas-bpn", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Bpn -  invalidated cache - allEntries");
    }
}
