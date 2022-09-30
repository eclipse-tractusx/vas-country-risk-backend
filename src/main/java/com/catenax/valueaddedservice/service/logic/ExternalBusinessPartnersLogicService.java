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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUser) {
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

    public List<String> getExternalPartnersCountry (CompanyUserDTO companyUserDTO) {
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUserDTO);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));
        return countryList;
    }

    public Long getTotalBpnByCountry(CountryDTO countryDTO,CompanyUserDTO companyUserDTO){
        final Long[] total = {0L};
        List<BusinessPartnerDTO> businessPartnerDTOS = getExternalBusinessPartners(companyUserDTO);
        Long value = businessPartnerDTOS.stream().filter(businessPartnerDTO -> businessPartnerDTO.getCountry().equalsIgnoreCase(countryDTO.getCountry())).count();
        return value;
    }
}
