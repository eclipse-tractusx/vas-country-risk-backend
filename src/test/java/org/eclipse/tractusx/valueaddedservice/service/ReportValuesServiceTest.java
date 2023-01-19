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

import org.eclipse.tractusx.valueaddedservice.domain.ReportValues;
import org.eclipse.tractusx.valueaddedservice.dto.ReportValuesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.ReportValuesRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.ReportMapper;
import org.eclipse.tractusx.valueaddedservice.service.mapper.ReportValuesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportValuesServiceTest {

    @Mock
    private ReportValuesRepository reportValuesRepository;

    @Mock
    private ReportValuesMapper reportValuesMapper;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportValuesService reportValuesService;

    @Test
    @DisplayName("Should return all the reportvalues")
    void findAllShouldReturnAllReportValues() {
        ReportValues reportValues = new ReportValues();
        reportValues.setId(1L);
        reportValues.setName("Range");
        reportValues.setObjectValue(Collections.singletonList("{\"min\":\"1\",\"max\":\"10\"}"));

        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setId(1L);
        reportValuesDTO.setName("Range");
        reportValuesDTO.setObjectValue(Collections.singletonList("{\"min\":\"1\",\"max\":\"10\"}"));

        Page<ReportValues> reportValuesPage = new PageImpl<>(List.of(reportValues));

        when(reportValuesRepository.findAll(any(Pageable.class))).thenReturn(reportValuesPage);
        when(reportValuesMapper.toDto(any(ReportValues.class))).thenReturn(reportValuesDTO);

        Page<ReportValuesDTO> result = reportValuesService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should delete the reportvalues when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        reportValuesService.delete(id);
        verify(reportValuesRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(reportValuesRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ReportValuesDTO> reportValuesDTO = reportValuesService.findOne(id);

        assertFalse(reportValuesDTO.isPresent());
    }

    @Test
    @DisplayName("Should throw an exception when the report is null")
    void saveWhenReportIsNullThenThrowException() {
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        assertThrows(
                NullPointerException.class, () -> reportValuesService.save(reportValuesDTO, null));
    }
}