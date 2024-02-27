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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceValueDTO;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceService;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DataSourceLogicService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    CompanyUserLogicService companyUserLogicService;

    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#year,#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year, CompanyUserDTO companyUserDTO){
        String sanitizedCompany = StringEscapeUtils.escapeJava(companyUserDTO.toString());
        log.debug("findRatingsByYearAndCompanyUser {}",sanitizedCompany);
        List<DataSourceDTO> dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        List<DataSourceDTO> companyRatings = dataSourceService.findByYearPublishedAndCompanyUserCompanyNameAndType(year, companyUserDTO, Type.Company);
        List<DataSourceDTO> dataSourceDTOByYearAndUser = dataSourceService.findRatingByYearAndUser(year,companyUserDTO);
        dataSourceDTOS.addAll(dataSourceDTOByYearAndUser);
        dataSourceDTOS.addAll(companyRatings);
        return dataSourceDTOS;
    }

    
    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByCompanyUser(CompanyUserDTO companyUserDTO){
        String sanitizedCompany = StringEscapeUtils.escapeJava(companyUserDTO.toString());
        log.debug("findRatingsByCompanyUser {}",sanitizedCompany);
        return dataSourceService.findRatingByUser(companyUserDTO);

    }

    @CacheEvict(value = "vas-datasource", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Datasource -  invalidated cache - allEntries");
    }

    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#year,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByYearAndCompanyUserCompany(Integer year, CompanyUserDTO companyUserDTO){
        String sanitizedCompany = StringEscapeUtils.escapeJava(companyUserDTO.toString());
        log.debug("findRatingsByYearAndCompanyUser {}",sanitizedCompany);
        List<DataSourceDTO>  dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        List<DataSourceDTO> companyRatings = dataSourceService.findByYearPublishedAndCompanyUserCompanyNameAndType(year, companyUserDTO, Type.Company);
        dataSourceDTOS.addAll(companyRatings);
        return dataSourceDTOS;
    }

    public void deleteDataSourceById(Long ratingId,CompanyUserDTO companyUserDTO){
        Optional<DataSourceDTO> optionalDataSourceDTO = dataSourceService.findOne(ratingId);
        DataSourceDTO dataSourceDTO = optionalDataSourceDTO.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        if( (dataSourceDTO.getCompanyUser().getCompanyName().equalsIgnoreCase(companyUserDTO.getCompanyName())
                && dataSourceDTO.getCompanyUser().getName().equalsIgnoreCase(companyUserDTO.getName())
                    && dataSourceDTO.getCompanyUser().getEmail().equalsIgnoreCase(companyUserDTO.getEmail()))
                || companyUserLogicService.isAdmin()){
            List<DataSourceValueDTO> dataSourceValueDTOS = dataSourceValueService.findByDataSource(dataSourceDTO);
            dataSourceValueDTOS.forEach(dataSourceValueDTO -> dataSourceValueService.delete(dataSourceValueDTO.getId()));
            dataSourceService.delete(dataSourceDTO.getId());
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
