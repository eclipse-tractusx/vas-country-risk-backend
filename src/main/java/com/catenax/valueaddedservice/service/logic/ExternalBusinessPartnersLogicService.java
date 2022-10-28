package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#companyUser.name,#companyUser.email,#companyUser.company}}", unless = "#result == null")
    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUser) {
        log.debug("getExternalBusinessPartners for companyUserDTO {}",companyUser);
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(applicationVariables.getToken());
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            //invokeService.executeRequest(legalEntitiesUrl, HttpMethod.GET,httpEntity,new Object());
            return objectMapper.readValue(json.getInputStream(), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<String> getExternalPartnersCountry (CompanyUserDTO companyUserDTO) {
        log.debug("getExternalPartnersCountry for companyUserDTO {}",companyUserDTO);
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUserDTO);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));
        return countryList;
    }

    
    @Cacheable(value = "vas-bpn", key = "{#root.methodName , {#countryDTO.iso3, #companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
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
