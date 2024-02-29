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

import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.dto.AuthPropertiesDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CountryDTO;
import org.eclipse.tractusx.valueaddedservice.service.CountryService;
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

    @Mock
    ApplicationVariables applicationVariables;


    @Test
    @DisplayName("Should return a list of countries filtered by iso2")
    void getCountryFilterByISO2ShouldReturnListOfCountriesFilteredByISO2() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        CountryDTO countryDTO1 = new CountryDTO(1L, "Germany", "DEU", "DE", "Europe", "", "", 3L);
        CountryDTO countryDTO2 = new CountryDTO(2L, "Spain", "ESP", "ES", "Europe", "", "", 3L);
        CountryDTO countryDTO3 = new CountryDTO(3L, "France", "FRA", "FR", "Europe", "", "", 3L);

        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("TestCompany");
        authPropertiesDTO.setEmail("test@email.com");
        authPropertiesDTO.setName("TestName");


        when(applicationVariables.getToken()).thenReturn("");

        List<CountryDTO> countryDTOList = Arrays.asList(countryDTO1, countryDTO2, countryDTO3);
        when(countryService.findAll()).thenReturn(countryDTOList);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO1, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(1L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO2, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(2L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO3, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(3L);

        List<CountryDTO> result = countryLogicService.getCountryFilterByISO2(companyUserDTO, applicationVariables.getToken(), new ArrayList<>());

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
        when(applicationVariables.getToken()).thenReturn("");

        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO1, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(1L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO2, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(2L);
        when(externalBusinessPartnersLogicService.getTotalBpnByCountry(countryDTO3, companyUserDTO, applicationVariables.getToken(), new ArrayList<>()))
                .thenReturn(3L);

        List<CountryDTO> result = countryLogicService.getCountryFilterByISO2(companyUserDTO, "", new ArrayList<>());

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
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        List<CountryDTO> countryDTOList =
                countryLogicService.getAssociatedCountries(companyUserDTO, "", new ArrayList<>());
        assertTrue(countryDTOList.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of countries when the companyuserdto is not null")
    void getAssociatedCountriesWhenCompanyUserDTONotNullThenReturnListOfCountries() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("john@email.com");
        companyUserDTO.setCompanyName("TestCompany");


        List<CountryDTO> countryDTOList = new ArrayList<>();
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountry("Germany");
        countryDTO.setIso2("DE");
        countryDTO.setIso3("DEU");
        countryDTO.setContinent("Europe");
        countryDTOList.add(countryDTO);

        when(countryService.findByCountryIn(anyList())).thenReturn(countryDTOList);

        List<CountryDTO> result = countryLogicService.getAssociatedCountries(companyUserDTO, "", new ArrayList<>());

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

    @Test
    @DisplayName("Should return a non country when the country non exists")
    void findCountryByNameWhenCountryNonExistsThenReturnCountry() {
        String countryName = "Germany";
        CountryDTO countryDTO =
                new CountryDTO(1L, "NonExist", "DEU", "DE", "Europe", null, null, null);
        when(countryService.findCountryByName(countryName)).thenReturn(Optional.empty());

        CountryDTO result = countryLogicService.findCountryByName(countryName);

        assertNotNull(result);
        assertNotEquals(countryDTO, result);
    }


}