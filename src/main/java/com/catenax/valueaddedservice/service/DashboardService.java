package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
@Slf4j
public class DashboardService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    ObjectMapper objectMapper;


    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<String> dataSources, CompanyUser companyUser) {
        log.debug("Request to get Table Info");
        List<DashBoardTableDTO> dataSourceDTOS = new ArrayList<>();
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUser);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));

        if (!dataSources.isEmpty()) {
            if (year != null && year > 0) {
                dataSourceDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, year);
            } else {
                dataSourceDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThan(Float.valueOf(-1), countryList, dataSources);
            }
        }


        return mapBusinessPartnerToDashboard(businessPartnerDTOS, dataSourceDTOS);
    }

    public List<DashBoardTableDTO> mapBusinessPartnerToDashboard(List<BusinessPartnerDTO> businessPartnerDTOS, List<DashBoardTableDTO> dataSourceDTOS) {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        final DashBoardTableDTO[] dashBoardTableDTO = {new DashBoardTableDTO()};
        businessPartnerDTOS.forEach(businessPartnerDTO -> {
            if (dataSourceDTOS.isEmpty()) {
                dashBoardTableDTO[0] = setBusinessPartnerProps(businessPartnerDTO,dashBoardTableDTO[0]);
                dashBoardTableDTOS.add(dashBoardTableDTO[0]);
            } else {
                dataSourceDTOS.forEach(element -> {
                    if (element.getCountry().equalsIgnoreCase(businessPartnerDTO.getCountry())) {
                        dashBoardTableDTO[0] = setBusinessPartnerProps(businessPartnerDTO,dashBoardTableDTO[0]);
                        dashBoardTableDTO[0].setRating(element.getRating());
                        dashBoardTableDTO[0].setScore(element.getScore());
                        dashBoardTableDTOS.add(dashBoardTableDTO[0]);
                    }
                });
            }
        });


        return dashBoardTableDTOS;
    }

    private DashBoardTableDTO setBusinessPartnerProps(BusinessPartnerDTO businessPartnerDTO,DashBoardTableDTO dashBoardTableDTO) {
        dashBoardTableDTO = new DashBoardTableDTO();
        dashBoardTableDTO.setBpn(businessPartnerDTO.getBpn());
        dashBoardTableDTO.setCity(businessPartnerDTO.getCity());
        dashBoardTableDTO.setCountry(businessPartnerDTO.getCountry());
        dashBoardTableDTO.setAddress(businessPartnerDTO.getAddress());
        return dashBoardTableDTO;
    }

    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUser companyUser) {
        try {

            return objectMapper.readValue(json.getInputStream(), new TypeReference<List<BusinessPartnerDTO>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
