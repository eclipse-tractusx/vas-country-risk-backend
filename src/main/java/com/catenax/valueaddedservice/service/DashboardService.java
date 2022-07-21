package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
import com.catenax.valueaddedservice.dto.DataDTO;
import com.catenax.valueaddedservice.dto.RatingDTO;
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

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUser companyUser) {
        log.debug("Request to get Table Info");
        List<String> dataSources = ratingDTOList.stream().map(each -> each.getDataSourceName()).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();
        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = getExternalBusinessPartners(companyUser);
        List<String> countryList = new ArrayList<>();
        countryList.addAll(businessPartnerDTOS.stream().map(BusinessPartnerDTO::getCountry)
                .collect(Collectors.toSet()));

        if (!dataSources.isEmpty()) {
            if (year != null && year > 0) {
                dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, year);
            } else {
                dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThan(Float.valueOf(-1), countryList, dataSources);
            }
        }

        dataDTOS.forEach(each->{
            ratingDTOList.forEach(eachData->{
                if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                    each.setWeight(eachData.getWeight());
                }
            });
        });
        return mapBusinessPartnerToDashboard(businessPartnerDTOS,dataDTOS,ratingDTOList);
    }

    public List<DashBoardTableDTO> mapBusinessPartnerToDashboard(List<BusinessPartnerDTO> businessPartnerDTOS,  List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS) {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        final DashBoardTableDTO[] dashBoardTableDTO = {new DashBoardTableDTO()};
        final int[] id = {1};
        businessPartnerDTOS.forEach(businessPartnerDTO -> {
            dashBoardTableDTO[0] = setBusinessPartnerProps(businessPartnerDTO,id[0]);
            id[0]++;
            dashBoardTableDTOS.add(dashBoardTableDTO[0]);
        });

        for(DashBoardTableDTO d: dashBoardTableDTOS){
            mapScoreForEachBpn(d,dataDTOS,ratingDTOS);
        }

        return dashBoardTableDTOS;
    }

    private DashBoardTableDTO mapScoreForEachBpn(DashBoardTableDTO d, List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS){
        List<DataDTO> dataSourceForCountry = dataDTOS.stream().filter(each -> each.getCountry().equalsIgnoreCase(d.getCountry())).collect(Collectors.toList());
        float total = 100F;
        float eachWeight = total/dataSourceForCountry.size();
        final float[] generalFormulaTotal = {0F};
        final String[] ratingsList = {""};
        dataSourceForCountry.stream().forEach(eachDataSource -> {
            if(ratingDTOS.size() == dataSourceForCountry.size()){
                generalFormulaTotal[0] = generalFormulaTotal[0] + (eachDataSource.getScore() * (eachDataSource.getWeight() * 0.01F));
            }else{
                generalFormulaTotal[0] = generalFormulaTotal[0] + (eachDataSource.getScore() * (eachWeight * 0.01F));
            }
            ratingsList[0] = concatenateRatings(ratingsList[0], eachDataSource);
        });
        d.setScore(generalFormulaTotal[0]);
        d.setRating(ratingsList[0]);
        return d;
    }

    private DashBoardTableDTO setBusinessPartnerProps(BusinessPartnerDTO businessPartnerDTO,Integer id) {
        DashBoardTableDTO dashBoardTableDTO = new DashBoardTableDTO();
        dashBoardTableDTO.setBpn(businessPartnerDTO.getBpn());
        dashBoardTableDTO.setCity(businessPartnerDTO.getCity());
        dashBoardTableDTO.setCountry(businessPartnerDTO.getCountry());
        dashBoardTableDTO.setAddress(businessPartnerDTO.getAddress());
        dashBoardTableDTO.setLegalName(businessPartnerDTO.getLegalName());
        dashBoardTableDTO.setId(Long.valueOf(id));
        return dashBoardTableDTO;
    }

    private String concatenateRatings(String ratingsList, DataDTO element) {
        if (ratingsList.isEmpty()) {
            ratingsList = element.getDataSourceName();
        } else {
            ratingsList = ratingsList + "," + element.getDataSourceName();
        }

        return ratingsList;
    }

    private Float calculateFinalScore(Float score, DashBoardTableDTO element) {

        return score;
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
