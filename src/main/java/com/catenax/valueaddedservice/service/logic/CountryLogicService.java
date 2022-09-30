package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.catenax.valueaddedservice.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CountryLogicService {

    @Autowired
    CountryService countryService;


    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    public List<CountryDTO> getAssociatedCountries (CompanyUserDTO companyUserDTO) {
        List<String> countryList;
        countryList = externalBusinessPartnersLogicService.getExternalPartnersCountry(companyUserDTO);

        List<CountryDTO> countryDTOS;
        countryDTOS = countryService.findCountryByName(countryList);

        return countryDTOS;
    }


    public List<CountryDTO> getCountryFilterByISO2(CompanyUserDTO companyUserDTO){

        List<CountryDTO> countryDTOList = countryService.findAll().stream().filter(distinctByKey(CountryDTO::getIso2)).collect(Collectors.toList());
        countryDTOList.forEach(countryDTO -> countryDTO.setTotalBpn(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO,companyUserDTO)));

        return countryDTOList;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
