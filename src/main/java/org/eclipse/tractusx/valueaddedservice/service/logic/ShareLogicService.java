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
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.ShareDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.ShareRatingDTO;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class  ShareLogicService {

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    @Autowired
    BusinessPartnersLogicService businessPartnersLogicService;

    public List<ShareDTO> findRatingsScoresForEachBpn(List<DataSourceDTO> datasource, List<BusinessPartnerDTO> businessPartnerToMap, CompanyUserDTO companyUser,
                                                      String token,List<String> roles) {

        List<ShareDTO> shareDTOSList = new ArrayList<>();
        
        List<BusinessPartnerDTO> companyBusinessPartnerDTOS;
        companyBusinessPartnerDTOS = businessPartnersLogicService.getExternalBusinessPartners(companyUser,token,roles);

        businessPartnerToMap.forEach(bp -> {
            if(bp.getCountry() == null){
                companyBusinessPartnerDTOS.forEach(bpDTO -> {
                   if(bp.getBpn().equals(bpDTO.getBpn())){
                       bp.setCountry(bpDTO.getCountry());
                   }
                });
            }
        });

        List<String> countryList = new ArrayList<>();;
        countryList.addAll(businessPartnerToMap.stream().map(BusinessPartnerDTO::getCountry).collect(Collectors.toSet()));

        List<String> dataSources = datasource.stream().map(DataSourceDTO::getDataSourceName).collect(Collectors.toList());
        List<DataDTO> dataDTOS = new ArrayList<>();

        datasource.forEach(ds -> {
            if (!dataSources.isEmpty() && ds.getYearPublished() != null && ds.getYearPublished() > 0) {
                dataDTOS.addAll(dataSourceValueService.findByRatingAndCountryAndScoreGreaterThanAndYear(Float.valueOf(-1), countryList, dataSources, ds.getYearPublished()));
            }
        });

        final ShareDTO[] shareDTOS = {new ShareDTO()};
        final int[] id = {1};
        businessPartnerToMap.forEach(bp -> {
            shareDTOS[0] = setShareDTO(bp,dataDTOS,id[0]);
            id[0]++;
            shareDTOSList.add(shareDTOS[0]);
        });
        
        return shareDTOSList;
    }

    private ShareDTO setShareDTO(BusinessPartnerDTO bp, List<DataDTO> dataDTOS, Integer id ) {
        List<ShareRatingDTO> shareRatingDTOList = new ArrayList<>();
        ShareDTO shareDTO = new ShareDTO();
        shareDTO.setId(Long.valueOf(id));
        shareDTO.setBpn(bp.getBpn());
        shareDTO.setCountry(bp.getCountry());
        dataDTOS.forEach(dataDTO -> {
            if(Objects.equals(dataDTO.getCountry(), bp.getCountry())){
                ShareRatingDTO shareRatingDTO = new ShareRatingDTO();
                shareRatingDTO.setDataSourceName(dataDTO.getDataSourceName());
                shareRatingDTO.setScore(dataDTO.getScore());
                shareRatingDTOList.add(shareRatingDTO);
                shareDTO.setIso2(dataDTO.getIso2());
            }
        });
        shareDTO.setRating(shareRatingDTOList);
        return shareDTO;
    }
}
