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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.domain.DataSource;
import org.eclipse.tractusx.valueaddedservice.dto.*;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceValueService;
import org.eclipse.tractusx.valueaddedservice.utils.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Slf4j
public class WorldMapAndTableLogicService {

    @Autowired
    DataSourceValueService dataSourceValueService;


    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    @Autowired
    BusinessPartnersLogicService businessPartnersLogicService;


    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser,String token,List<String> roles) {
        log.debug("Request to get Table Info");
        List<String> dataSources = ratingDTOList.stream().map(RatingDTO::getDataSourceName).toList();
        List<DataDTO> dataDTOS = new ArrayList<>();

        List<BusinessPartnerDTO> businessPartnerDTOS;
        businessPartnerDTOS = businessPartnersLogicService.getExternalBusinessPartners(companyUser,token,roles);
        List<String> countryList = externalBusinessPartnersLogicService.getExternalPartnersCountry(companyUser,token,roles);

        if (!dataSources.isEmpty() && year != null && year > 0) {
            dataDTOS = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, year);
        }

        dataDTOS.forEach(each-> ratingDTOList.forEach(eachData->{
            if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                each.setWeight(eachData.getWeight());
            }
        }));
        return mapBusinessPartnerToDashboard(businessPartnerDTOS,dataDTOS,ratingDTOList);
    }

    public List<DashBoardWorldMapDTO> getWorldMapInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        log.debug("Request to get WorldMap Info");
        List<String> dataSources = ratingDTOList.stream().map(RatingDTO::getDataSourceName).toList();
        List<DataDTO> dataDTOS = new ArrayList<>();

        if (!dataSources.isEmpty() && year != null && year > 0) {
            dataDTOS = dataSourceValueService.findByRatingAndScoreGreaterThanAndYear(Float.valueOf(-1), dataSources, year);
        }

        dataDTOS.forEach(each-> ratingDTOList.forEach(eachData->{
            if(each.getDataSourceName().equalsIgnoreCase(eachData.getDataSourceName())){
                each.setWeight(eachData.getWeight());
            }
        }));
        return mapDataSourcesToWorldMap(dataDTOS,ratingDTOList,companyUser);
    }

    private List<DashBoardWorldMapDTO> mapDataSourcesToWorldMap(List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS,CompanyUserDTO companyUser){
        List<DataDTO> countryList = dataDTOS.stream().filter(MethodUtils.distinctByKey(DataDTO::getIso2)).toList();
        final CountryDTO[] countryDTO = {new CountryDTO()};
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS = new ArrayList<>();
        final DashBoardWorldMapDTO[] dashBoardWorldMapDTO = {new DashBoardWorldMapDTO()};
        countryList.forEach(country->{
            countryDTO[0] = new CountryDTO(country.getCountry(),country.getIso3(),country.getIso2(),country.getContinent());
            final float[] generalFormulaTotal = {0F};
            final float[] totalRatedByUser = {0F};
            List<DataDTO> dataSources = dataDTOS.stream().filter(dataDTO -> dataDTO.getCountry().equalsIgnoreCase(country.getCountry())).toList();
            dataSources.forEach(each -> totalRatedByUser[0] = totalRatedByUser[0] + each.getWeight());
            dataSources.forEach(dataDTO -> generalFormulaTotal[0] = generalFormulaTotal[0] + calculateFinalScore(ratingDTOS.size(),dataSources.size(),dataDTO,totalRatedByUser[0]));
            dashBoardWorldMapDTO[0] = new DashBoardWorldMapDTO();
            dashBoardWorldMapDTO[0].setCountry(countryDTO[0]);
            dashBoardWorldMapDTO[0].setScore(generalFormulaTotal[0]);

            dashBoardWorldMapDTOS.add(dashBoardWorldMapDTO[0]);
        });
        return dashBoardWorldMapDTOS;
    }

    private List<DashBoardTableDTO> mapBusinessPartnerToDashboard(List<BusinessPartnerDTO> businessPartnerDTOS,  List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS) {
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

    private Float calculateFinalScore(Integer ratingDTOSize, Integer dataSourceCountrySize,DataDTO eachDataSource,Float totalRatedByUser) {
        Float generalFormulaTotal = 0F;
        if(ratingDTOSize.equals(dataSourceCountrySize)){
            generalFormulaTotal= generalFormulaTotal + (eachDataSource.getScore() * (eachDataSource.getWeight() * 0.01F));
        }else{
            Float eachWeight = eachDataSource.getWeight() * 100.00F / totalRatedByUser ;
            generalFormulaTotal = generalFormulaTotal+ (eachDataSource.getScore() * (eachWeight * 0.01F));
        }

        return MethodUtils.formatFloatTwoDecimals(generalFormulaTotal);
    }

    private DashBoardTableDTO setBusinessPartnerProps(BusinessPartnerDTO businessPartnerDTO,Integer id) {
        DashBoardTableDTO dashBoardTableDTO = new DashBoardTableDTO();
        dashBoardTableDTO.setBpn(businessPartnerDTO.getBpn());
        dashBoardTableDTO.setCity(businessPartnerDTO.getCity());
        dashBoardTableDTO.setCountry(businessPartnerDTO.getCountry());

        dashBoardTableDTO.setStreet(businessPartnerDTO.getStreet());
        dashBoardTableDTO.setHouseNumber(businessPartnerDTO.getHouseNumber());
        dashBoardTableDTO.setZipCode(businessPartnerDTO.getZipCode());
        dashBoardTableDTO.setLegalName(businessPartnerDTO.getLegalName());
        dashBoardTableDTO.setId(Long.valueOf(id));
        dashBoardTableDTO.setLatitude(businessPartnerDTO.getLatitude());
        dashBoardTableDTO.setLongitude(businessPartnerDTO.getLongitude());
        dashBoardTableDTO.setCustomer(businessPartnerDTO.getCustomer());
        dashBoardTableDTO.setSupplier(businessPartnerDTO.getSupplier());
        return dashBoardTableDTO;
    }

    private DashBoardTableDTO mapScoreForEachBpn(DashBoardTableDTO d, List<DataDTO> dataDTOS,List<RatingDTO> ratingDTOS){
        List<DataDTO> dataSourceForCountry = dataDTOS.stream().filter(each -> each.getCountry().equalsIgnoreCase(d.getCountry())).toList();
        final float[] generalFormulaTotal = {0F};
        final String[] ratingsList = {""};
        final float[] totalRatedByUser = {0F};
        dataSourceForCountry.forEach(each -> totalRatedByUser[0] = totalRatedByUser[0] + each.getWeight());
        dataSourceForCountry.stream().forEach(eachDataSource -> {
            generalFormulaTotal[0] = generalFormulaTotal[0] + calculateFinalScore(ratingDTOS.size(),dataSourceForCountry.size(),eachDataSource,totalRatedByUser[0]);
            ratingsList[0] = concatenateRatings(ratingsList[0], eachDataSource);
        });
        d.setScore(generalFormulaTotal[0]);
        d.setRating(ratingsList[0]);
        return d;
    }

    private String concatenateRatings(String ratingsList, DataDTO element) {
        if (ratingsList.isEmpty()) {
            ratingsList = element.getDataSourceName();
        } else {
            ratingsList = ratingsList + "," + element.getDataSourceName();
        }

        return ratingsList;
    }


}
