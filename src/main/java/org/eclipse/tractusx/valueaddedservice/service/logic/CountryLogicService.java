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
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CountryDTO;
import org.eclipse.tractusx.valueaddedservice.service.CountryService;
import org.eclipse.tractusx.valueaddedservice.utils.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryLogicService {



    @Autowired
    CountryService countryService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;


    @Cacheable(value = "vas-country", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName},#roles }", unless = "#result == null")
    public List<CountryDTO> getAssociatedCountries (CompanyUserDTO companyUserDTO,String token,List<String> roles) {
        log.debug("getAssociatedCountries filtered by companyUserDTO " + companyUserDTO);
        List<String> countryList;
        countryList = externalBusinessPartnersLogicService.getExternalPartnersCountry(companyUserDTO,token,roles);

        List<CountryDTO> countryDTOS;
        countryDTOS = countryService.findByCountryIn(countryList);

        return countryDTOS;
    }


    
    @Cacheable(value = "vas-country", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName},#roles}", unless = "#result == null")
    public List<CountryDTO> getCountryFilterByISO2(CompanyUserDTO companyUserDTO,String token,List<String> roles){
        log.debug("getCountryFilterByISO2 filtered by companyUserDTO "+ companyUserDTO);
        List<CountryDTO> countryDTOList = countryService.findAll().stream().filter(MethodUtils.distinctByKey(CountryDTO::getIso2)).toList();
        countryDTOList.forEach(countryDTO -> countryDTO.setTotalBpn(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO,companyUserDTO,token,roles)));

        return countryDTOList;
    }

    
    @Cacheable(value = "vas-country", key = "{#root.methodName , #countryName}", unless = "#result == null")
    public CountryDTO findCountryByName(String countryName){
        Optional<CountryDTO> countryDTO = countryService.findCountryByName(countryName);
        log.debug("findCountryByName filtered by countryName");
        if(countryDTO.isPresent()){
            return countryDTO.get();
        }else{
            log.error("Country does not exists on country table");
            return new CountryDTO();
        }

    }

    @Cacheable(value = "vas-country", key = "{#root.methodName , #iso2}", unless = "#result == null")
    public CountryDTO findCountryByIso2(String iso2){
        Optional<CountryDTO> countryDTO = countryService.findCountryByIso2(iso2);
        log.debug("findCountryByIso2 filtered by iso2");
        if(countryDTO.isPresent()){
            return countryDTO.get();
        }else{
            log.error("Country does not exists on country table");
            return new CountryDTO();
        }

    }


    @CacheEvict(value = "vas-country", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Country -  invalidated cache - allEntries");
    }


}
