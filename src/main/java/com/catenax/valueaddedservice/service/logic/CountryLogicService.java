package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.catenax.valueaddedservice.service.CountryService;
import com.catenax.valueaddedservice.utils.MethodUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CountryLogicService {

    @Autowired
    CountryService countryService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;


    @Cacheable(value = "vas-country", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<CountryDTO> getAssociatedCountries (CompanyUserDTO companyUserDTO) {
        log.debug("getAssociatedCountries filtered by companyUserDTO {} ",companyUserDTO);
        List<String> countryList;
        countryList = externalBusinessPartnersLogicService.getExternalPartnersCountry(companyUserDTO);

        List<CountryDTO> countryDTOS;
        countryDTOS = countryService.findByCountryIn(countryList);

        return countryDTOS;
    }


    
    @Cacheable(value = "vas-country", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<CountryDTO> getCountryFilterByISO2(CompanyUserDTO companyUserDTO){
        log.debug("getCountryFilterByISO2 filtered by companyUserDTO {} ",companyUserDTO);
        List<CountryDTO> countryDTOList = countryService.findAll().stream().filter(MethodUtils.distinctByKey(CountryDTO::getIso2)).collect(Collectors.toList());
        countryDTOList.forEach(countryDTO -> countryDTO.setTotalBpn(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO,companyUserDTO)));

        return countryDTOList;
    }

    
    @Cacheable(value = "vas-country", key = "{#root.methodName , #countryName}", unless = "#result == null")
    public CountryDTO findCountryByName(String countryName){
        Optional<CountryDTO> countryDTO = countryService.findCountryByName(countryName);
        log.debug("findCountryByName filtered by countryName {} ",countryName);
        if(countryDTO.isPresent()){
            return countryDTO.get();
        }else{
            log.error("Country does not exists on country table {}",countryName);
            return new CountryDTO();
        }


    }

    @CacheEvict(value = "vas-country", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Country -  invalidated cache - allEntries");
    }


}
