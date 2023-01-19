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
package org.eclipse.tractusx.valueaddedservice.service;

import org.eclipse.tractusx.valueaddedservice.domain.RegionValue;
import org.eclipse.tractusx.valueaddedservice.dto.RegionValueDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RegionValueRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.RegionValueMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionValueServiceTest {

    @Mock
    private RegionValueRepository regionValueRepository;

    @Mock
    private RegionValueMapper regionValueMapper;

    @InjectMocks
    private RegionValueService regionValueService;

    @Test
    @DisplayName("Should return all RegionValues")
    void findAllShouldReturnAllRegionValues() {
        Pageable pageable = PageRequest.of(0, 10);
        List<RegionValue> regionList = new ArrayList<>();
        regionList.add(new RegionValue());
        regionList.add(new RegionValue());
        Page<RegionValue> RegionValuePage = new PageImpl<>(regionList);

        when(regionValueRepository.findAll(any(Pageable.class))).thenReturn(RegionValuePage);

        Page<RegionValueDTO> result = regionValueService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty when the RegionValue does not exist")
    void partialUpdateWhenRegionValueDoesNotExistThenReturnEmpty() {
        RegionValueDTO regionDTO = new RegionValueDTO();
        regionDTO.setId(1L);
        when(regionValueRepository.findById(any())).thenReturn(Optional.empty());

        Optional<RegionValueDTO> result = regionValueService.partialUpdate(regionDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the updated RegionValue when the RegionValue exists")
    void partialUpdateWhenRegionValueExistsThenReturnUpdatedRegionValue() {
        RegionValueDTO regionValueDTO = new RegionValueDTO();
        regionValueDTO.setId(1L);
        regionValueDTO.setContinent("Continent");
        regionValueDTO.setCountry("Country");



        RegionValue regionValue = new RegionValue();
        regionValue.setId(1L);
        regionValue.setContinent("Continent");
        regionValue.setCountry("Country");

        when(regionValueRepository.findById(anyLong())).thenReturn(Optional.of(regionValue));
        when(regionValueRepository.save(any())).thenReturn(regionValue);
        when(regionValueMapper.toDto(regionValue)).thenReturn(regionValueDTO);

        Optional<RegionValueDTO> result = regionValueService.partialUpdate(regionValueDTO);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), regionValueDTO.getId());
    }

    @Test
    @DisplayName("Should return the RegionValue when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        RegionValue regionValue = new RegionValue();
        regionValue.setId(id);
        RegionValueDTO regionValueDTO = new RegionValueDTO();
        regionValueDTO.setId(id);

        when(regionValueRepository.findById(id)).thenReturn(Optional.of(regionValue));
        when(regionValueMapper.toDto(regionValue)).thenReturn(regionValueDTO);

        Optional<RegionValueDTO> result = regionValueService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }

    @Test
    @DisplayName("Should delete the RegionValue when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        regionValueService.delete(id);
        verify(regionValueRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should save the region")
    void saveShouldSaveFile() {
        RegionValueDTO regionValueDTO = new RegionValueDTO();
        RegionValue regionValue = new RegionValue();
        when(regionValueMapper.toEntity(regionValueDTO)).thenReturn(regionValue);
        when(regionValueRepository.save(regionValue)).thenReturn(regionValue);
        when(regionValueMapper.toDto(regionValue)).thenReturn(regionValueDTO);

        RegionValueDTO result = regionValueService.save(regionValueDTO);

        assertEquals(result, regionValueDTO);
    }

}