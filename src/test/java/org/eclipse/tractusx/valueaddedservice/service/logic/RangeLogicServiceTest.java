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

import org.eclipse.tractusx.valueaddedservice.domain.enumeration.RangeType;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.RangeDTO;
import org.eclipse.tractusx.valueaddedservice.service.RangeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RangeLogicServiceTest {

    @Mock
    RangeService rangeService;

    @InjectMocks
    RangeLogicService rangeLogicService;



    @Test
    @DisplayName("Should return the ranges when the user has ranges")
    void getUserRangesOrDefaultWhenUserHasRangesThenReturnTheRanges() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        RangeDTO rangeDTOMin = new RangeDTO();
        rangeDTOMin.setRange(RangeType.Min);
        rangeDTOMin.setCompanyUser(companyUserDTO);
        rangeDTOMin.setDescription("Min Range");
        rangeDTOMin.setValue(25);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUserDTO);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(50);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUserDTO);
        rangeDTOMax.setDescription("Max Range");
        rangeDTOMax.setValue(100);

        List<RangeDTO> ranges = Arrays.asList(rangeDTOMin, rangeDTOBetWeen, rangeDTOMax);

        when(rangeService.getUserRanges(companyUserDTO)).thenReturn(ranges);

        List<RangeDTO> result = rangeLogicService.getUserRangesOrDefault(companyUserDTO);

        assertEquals(ranges, result);
    }

    @Test
    @DisplayName("Should save the ranges when the user has no ranges")
    void saveRangesWhenUserHasNoRanges() {
        List<RangeDTO> rangeDTOS = new ArrayList<>();
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        RangeDTO rangeDTOMin = new RangeDTO();
        rangeDTOMin.setRange(RangeType.Min);
        rangeDTOMin.setCompanyUser(companyUserDTO);
        rangeDTOMin.setDescription("Min Range");
        rangeDTOMin.setValue(25);
        rangeDTOS.add(rangeDTOMin);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUserDTO);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(50);
        rangeDTOS.add(rangeDTOBetWeen);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUserDTO);
        rangeDTOMax.setDescription("Max Range");
        rangeDTOMax.setValue(100);
        rangeDTOS.add(rangeDTOMax);

        when(rangeService.getUserRanges(companyUserDTO)).thenReturn(new ArrayList<>());

        rangeLogicService.saveRanges(rangeDTOS, companyUserDTO);

        verify(rangeService, times(1)).getUserRanges(companyUserDTO);
    }

    @Test
    @DisplayName("Should update the ranges when the user has ranges")
    void saveRangesWhenUserHasRanges() {
        List<RangeDTO> rangeDTOS = new ArrayList<>();
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        RangeDTO rangeDTOMin = new RangeDTO();
        rangeDTOMin.setRange(RangeType.Min);
        rangeDTOMin.setCompanyUser(companyUserDTO);
        rangeDTOMin.setDescription("Min Range");
        rangeDTOMin.setValue(25);
        rangeDTOS.add(rangeDTOMin);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUserDTO);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(50);
        rangeDTOS.add(rangeDTOBetWeen);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUserDTO);
        rangeDTOMax.setDescription("Max Range");
        rangeDTOMax.setValue(100);
        rangeDTOS.add(rangeDTOMax);

        when(rangeService.getUserRanges(companyUserDTO)).thenReturn(rangeDTOS);

        rangeLogicService.saveRanges(rangeDTOS, companyUserDTO);

        verify(rangeService, times(1)).getUserRanges(companyUserDTO);
    }
}