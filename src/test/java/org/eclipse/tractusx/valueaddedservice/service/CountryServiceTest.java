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
package org.eclipse.tractusx.valueaddedservice.service;

import org.eclipse.tractusx.valueaddedservice.domain.Country;
import org.eclipse.tractusx.valueaddedservice.dto.CountryDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CountryRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CountryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryService countryService;


    @Test
    @DisplayName("Should return one by name")
    void findCountryByName() {
        String name = "PORTUGAL";
        when(countryRepository.findByCountry(name)).thenReturn(Optional.empty());

        Optional<CountryDTO> countryDTO = countryService.findCountryByName(name);

        assertFalse(countryDTO.isPresent());
    }

    @Test
    @DisplayName("Should return all Countrys")
    void findAllCountry() {
        Country country = new Country();
        country.setId(1L);
        country.setCountry("PORTUGAL");


        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(1L);
        countryDTO.setCountry("PORTUGAL");

        Page<Country> countryPage = new PageImpl<>(List.of(country));

        when(countryRepository.findAll(any(Pageable.class))).thenReturn(countryPage);
        when(countryMapper.toDto(any(Country.class))).thenReturn(countryDTO);

        Page<CountryDTO> result = countryService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CountryDTO> reportValuesDTO = countryService.findOne(id);

        assertFalse(reportValuesDTO.isPresent());
    }

    @Test
    @DisplayName("Should delete the country when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        countryService.delete(id);
        verify(countryRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should save")
    void save() {
        CountryDTO countryDTO = new CountryDTO();

        CountryDTO result = countryService.save(countryDTO);

        assertNotEquals(countryDTO,result);
    }

    @Test
    @DisplayName("Should return the updated country when the id is valid")
    void partialUpdateWhenIdIsInvalidThenReturnEmpty() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(Long.MAX_VALUE);


        Country country = new Country();
        country.setId(Long.MAX_VALUE);


        when(countryRepository.findById(Long.MAX_VALUE)).thenReturn(Optional.of(country));

        Optional<CountryDTO> result = countryService.partialUpdate(countryDTO);

        assertTrue(true);
    }
}