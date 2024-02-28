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

import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.service.DataSourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataSourceLogicService")
class DataSourceLogicServiceTest {

    @Mock
    DataSourceService dataSourceService;

    @InjectMocks
    DataSourceLogicService dataSourceLogicService;

    @Test
    @DisplayName("Should return an empty list when the companyuserdto is null")
    void findRatingsByCompanyUserWhenCompanyUserDTOIsNull() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        List<DataSourceDTO> dataSourceDTOS =
                dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO);
        assertTrue(dataSourceDTOS.isEmpty());
    }

    @Test
    @DisplayName("Should return the list of datasourcedto when the companyuserdto is not null")
    void findRatingsByCompanyUserWhenCompanyUserDTOIsNotNull() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        List<DataSourceDTO> dataSourceDTOS = new ArrayList<>();
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("Fake Rating");
        dataSourceDTO.setType(Type.Global);
        dataSourceDTO.setYearPublished(2021);
        dataSourceDTO.setFileName("Test Company Rating");
        dataSourceDTOS.add(dataSourceDTO);
        when(dataSourceService.findRatingByUser(companyUserDTO)).thenReturn(dataSourceDTOS);
        List<DataSourceDTO> result =
                dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO);
        assertNotNull(result);
        assertEquals(1, result.size());
    }



    @Test
    @DisplayName(
            "Should return a list of datasourcedto when the year and companyuserdto are not null")
    void
    findRatingsByYearAndCompanyUserWhenYearAndCompanyUserDTONotNullThenReturnListOfDataSourceDTO() {
        Integer year = 2020;
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        List<DataSourceDTO> dataSourceDTOS = new ArrayList<>();
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("Fake Rating");
        dataSourceDTO.setType(Type.Global);
        dataSourceDTO.setYearPublished(2020);
        dataSourceDTO.setFileName("Test Company Rating");
        dataSourceDTOS.add(dataSourceDTO);

        when(dataSourceService.findRatingsByYearAndTypeGlobal(year)).thenReturn(dataSourceDTOS);
        when(dataSourceService.findRatingByYearAndUser(year, companyUserDTO))
                .thenReturn(dataSourceDTOS);

        List<DataSourceDTO> result =
                dataSourceLogicService.findRatingsByYearAndCompanyUser(year, companyUserDTO);

        assertNotNull(result);
        assertEquals(2, result.size());
    }


}