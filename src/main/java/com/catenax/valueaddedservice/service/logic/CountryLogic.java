package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.Country;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.catenax.valueaddedservice.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CountryLogic {

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


}
