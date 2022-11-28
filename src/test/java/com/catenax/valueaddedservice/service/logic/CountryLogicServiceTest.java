package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.CountryDTO;
import com.catenax.valueaddedservice.service.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CountryLogicService")
class CountryLogicServiceTest {

    @Mock
    CountryService countryService;

    @Mock
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    @InjectMocks
    CountryLogicService countryLogicService;

    @Test
    @DisplayName("Should return a list of countries filtered by iso2")
    void getCountryFilterByISO2ShouldReturnListOfCountriesFilteredByISO2() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        CountryDTO countryDTO1 = new CountryDTO(1L, "Germany", "DEU", "DE", "Europe","","",3L);
        CountryDTO countryDTO2 = new CountryDTO(2L, "Spain", "ESP", "ES", "Europe","","",3L);
        CountryDTO countryDTO3 = new CountryDTO(3L, "France", "FRA", "FR", "Europe","","",3L);
        List<CountryDTO> countryDTOList = Arrays.asList(countryDTO1, countryDTO2, countryDTO3);
        when(countryService.findAll()).thenReturn(countryDTOList);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO1, companyUserDTO))
                .thenReturn(1L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO2, companyUserDTO))
                .thenReturn(2L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO3, companyUserDTO))
                .thenReturn(3L);

        List<CountryDTO> result = countryLogicService.getCountryFilterByISO2(companyUserDTO);

        assertEquals(3, result.size());
        assertEquals("Germany", result.get(0).getCountry());
        assertEquals("Spain", result.get(1).getCountry());
        assertEquals("France", result.get(2).getCountry());
    }

    @Test
    @DisplayName(
            "Should return a list of countries filtered by iso2 and the total of business partners")
    void getCountryFilterByISO2ShouldReturnListOfCountriesFilteredByISO2AndTotalBusinessPartners() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        CountryDTO countryDTO1 =
                new CountryDTO(1L, "Germany", "DEU", "DE", "Europe", null, null, null);
        CountryDTO countryDTO2 =
                new CountryDTO(2L, "Spain", "ESP", "ES", "Europe", null, null, null);
        CountryDTO countryDTO3 =
                new CountryDTO(3L, "France", "FRA", "FR", "Europe", null, null, null);
        List<CountryDTO> countryDTOList = Arrays.asList(countryDTO1, countryDTO2, countryDTO3);

        when(countryService.findAll()).thenReturn(countryDTOList);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO1, companyUserDTO))
                .thenReturn(1L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO2, companyUserDTO))
                .thenReturn(2L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO3, companyUserDTO))
                .thenReturn(3L);

        List<CountryDTO> result = countryLogicService.getCountryFilterByISO2(companyUserDTO);

        assertEquals(3, result.size());
        assertEquals("Germany", result.get(0).getCountry());
        assertEquals("Spain", result.get(1).getCountry());
        assertEquals("France", result.get(2).getCountry());
        assertEquals("DEU", result.get(0).getIso3());
        assertEquals("ESP", result.get(1).getIso3());
        assertEquals("FRA", result.get(2).getIso3());
        assertEquals("DE", result.get(0).getIso2());
        assertEquals("ES", result.get(1).getIso2());
        assertEquals("FR", result.get(2).getIso2());
        assertEquals("Europe", result.get(0).getContinent());
        assertEquals("Europe", result.get(1).getContinent());
        assertEquals("Europe", result.get(2).getContinent());
        assertEquals((Long) 1L, result.get(0).getTotalBpn());
        assertEquals((Long) 2L, result.get(1).getTotalBpn());
        assertEquals((Long) 3L, result.get(2).getTotalBpn());
    }

    @Test
    @DisplayName("Should return an empty list when the companyuserdto is null")
    void getAssociatedCountriesWhenCompanyUserDTONullThenReturnEmptyList() {
        CompanyUserDTO companyUserDTO = null;
        List<CountryDTO> countryDTOList =
                countryLogicService.getAssociatedCountries(companyUserDTO);
        assertTrue(countryDTOList.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of countries when the companyuserdto is not null")
    void getAssociatedCountriesWhenCompanyUserDTONotNullThenReturnListOfCountries() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("john@email.com");
        companyUserDTO.setCompanyName("TestCompany");

        List<String> countryList = new ArrayList<>();
        countryList.add("Germany");
        countryList.add("France");

        List<CountryDTO> countryDTOList = new ArrayList<>();
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountry("Germany");
        countryDTO.setIso2("DE");
        countryDTO.setIso3("DEU");
        countryDTO.setContinent("Europe");
        countryDTOList.add(countryDTO);

        when(countryService.findByCountryIn(anyList())).thenReturn(countryDTOList);

        List<CountryDTO> result = countryLogicService.getAssociatedCountries(companyUserDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
    }



    @Test
    @DisplayName("Should return a country when the country exists")
    void findCountryByNameWhenCountryExistsThenReturnCountry() {
        String countryName = "Germany";
        CountryDTO countryDTO =
                new CountryDTO(1L, "Germany", "DEU", "DE", "Europe", null, null, null);
        when(countryService.findCountryByName(countryName)).thenReturn(Optional.of(countryDTO));

        CountryDTO result = countryLogicService.findCountryByName(countryName);

        assertNotNull(result);
        assertEquals(countryDTO, result);
    }


}