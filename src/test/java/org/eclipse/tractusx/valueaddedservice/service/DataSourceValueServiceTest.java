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

import org.eclipse.tractusx.valueaddedservice.domain.DataSourceValue;
import org.eclipse.tractusx.valueaddedservice.dto.DataDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceValueDTO;
import org.eclipse.tractusx.valueaddedservice.repository.DataSourceValueRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.DataSourceValueMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSourceValueServiceTest {

    @Mock
    private DataSourceValueRepository dataSourceValueRepository;

    @Mock
    private DataSourceValueMapper dataSourceValueMapper;

    @InjectMocks
    private DataSourceValueService dataSourceValueService;


    @Test
    @DisplayName("Should update the datasourcevalue when the id is found")
    void partialUpdateWhenIdIsFound() {
        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        dataSourceValueDTO.setId(1L);
        dataSourceValueDTO.setScore(10F);
        DataSourceValue dataSourceValue = new DataSourceValue();
        dataSourceValue.setId(1L);
        dataSourceValue.setScore(10F);

        when(dataSourceValueRepository.findById(any())).thenReturn(Optional.of(dataSourceValue));
        when(dataSourceValueRepository.save(any())).thenReturn(dataSourceValue);
        when(dataSourceValueMapper.toDto(dataSourceValue)).thenReturn(dataSourceValueDTO);

        Optional<DataSourceValueDTO> result = dataSourceValueService.partialUpdate(dataSourceValueDTO);

        assertTrue(result.isPresent());
        assertEquals(10F, result.get().getScore());
    }


    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(dataSourceValueRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DataSourceValueDTO> result = dataSourceValueService.findOne(id);

        assertFalse(result.isPresent());
    }


    @Test
    @DisplayName("Should delete the DataSourcesValues when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        dataSourceValueService.delete(id);
        verify(dataSourceValueRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return all DataSourcesValues")
    void findAllDataSourcesValues() {
        DataSourceValue dataSourceValue = new DataSourceValue();
        dataSourceValue.setId(1L);
        dataSourceValue.setScore(10F);


        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        dataSourceValueDTO.setId(1L);
        dataSourceValueDTO.setScore(10F);

        Page<DataSourceValue> dataSourceValueDTOS = new PageImpl<>(List.of(dataSourceValue));

        when(dataSourceValueRepository.findAll(any(Pageable.class))).thenReturn(dataSourceValueDTOS);
        when(dataSourceValueMapper.toDto(any(DataSourceValue.class))).thenReturn(dataSourceValueDTO);

        Page<DataSourceValueDTO> result = dataSourceValueService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return a list of dataDto filtered by iso2")
    void findByRatingAndCountryAndScoreGreaterThan() {
        DataDTO dataDTO = new DataDTO("PORTUGAL", 10F, "TESTE", "TET", "TE", "TESTE");
        List<DataDTO> dataDTOList = Arrays.asList(dataDTO);
        when(dataSourceValueRepository.findByRatingAndCountryAndScoreGreaterThan(10F, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(dataDTOList);

        List<DataDTO> result = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThan(10F, new ArrayList<>(), new ArrayList<>());

        assertEquals(1, result.size());
        assertEquals("PORTUGAL", result.get(0).getCountry());
    }

    @Test
    @DisplayName("Should return a list of dataDto")
    void findByRatingAndScoreGreaterThan() {
        DataDTO dataDTO = new DataDTO("PORTUGAL", 10F, "TESTE", "TET", "TE", "TESTE");
        List<DataDTO> dataDTOList = Arrays.asList(dataDTO);
        when(dataSourceValueRepository.findByRatingAndScoreGreaterThan(new ArrayList<>(),10F))
                .thenReturn(dataDTOList);

        List<DataDTO> result = dataSourceValueService.findByRatingAndScoreGreaterThan(10F, new ArrayList<>());

        assertEquals(1, result.size());
        assertEquals("PORTUGAL", result.get(0).getCountry());
    }

}