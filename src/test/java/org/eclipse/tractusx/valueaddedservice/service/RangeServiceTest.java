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

import org.eclipse.tractusx.valueaddedservice.domain.Range;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.RangeType;
import org.eclipse.tractusx.valueaddedservice.dto.RangeDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RangeRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.RangeMapper;
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
class RangeServiceTest {

    @Mock
    private RangeRepository rangeRepository;

    @Mock
    private RangeMapper rangeMapper;

    @InjectMocks
    private RangeService rangeService;

    @Test
    @DisplayName("Should return all ranges")
    void findAllShouldReturnAllRanges() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Range> rangeList = new ArrayList<>();
        rangeList.add(new Range());
        rangeList.add(new Range());
        Page<Range> rangePage = new PageImpl<>(rangeList);

        when(rangeRepository.findAll(any(Pageable.class))).thenReturn(rangePage);

        Page<RangeDTO> result = rangeService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty when the range does not exist")
    void partialUpdateWhenRangeDoesNotExistThenReturnEmpty() {
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(1L);
        when(rangeRepository.findById(any())).thenReturn(Optional.empty());

        Optional<RangeDTO> result = rangeService.partialUpdate(rangeDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the updated range when the range exists")
    void partialUpdateWhenRangeExistsThenReturnUpdatedRange() {
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(1L);
        rangeDTO.setValue(80);
        rangeDTO.setDescription("Max value");
        rangeDTO.setRange(RangeType.Max);

        Range range = new Range();
        range.setId(1L);
        range.setValue(80);
        range.setDescription("Max value");
        range.setRange(RangeType.Max);

        when(rangeRepository.findById(anyLong())).thenReturn(Optional.of(range));
        when(rangeRepository.save(any())).thenReturn(range);
        when(rangeMapper.toDto(range)).thenReturn(rangeDTO);

        Optional<RangeDTO> result = rangeService.partialUpdate(rangeDTO);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), rangeDTO.getId());
    }

    @Test
    @DisplayName("Should return the Range when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        Range range = new Range();
        range.setId(id);
        RangeDTO rangeDTO = new RangeDTO();
        rangeDTO.setId(id);

        when(rangeRepository.findById(id)).thenReturn(Optional.of(range));
        when(rangeMapper.toDto(range)).thenReturn(rangeDTO);

        Optional<RangeDTO> result = rangeService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }

    @Test
    @DisplayName("Should delete the Range when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        rangeService.delete(id);
        verify(rangeRepository, times(1)).deleteById(id);
    }
}