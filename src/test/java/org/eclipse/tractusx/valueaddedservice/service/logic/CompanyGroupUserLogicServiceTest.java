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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.service.CompanyUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyGroupUserLogicServiceTest {

    @Mock
    CompanyUserService companyUserService;

    @InjectMocks
    CompanyUserLogicService companyUserLogicService;

    @Test
    @DisplayName("Should return the companyuser when the companyuser is already exist")
    void getOrCreateWhenCompanyUserIsAlreadyExistThenReturnTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setId(1L);
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");

        when(companyUserService.findByNameEmailAndCompany(companyUserDTO.getName(), companyUserDTO.getEmail(), companyUserDTO.getCompanyName()))
                .thenReturn(companyUserDTO);

        CompanyUserDTO companyUserDTOResult = companyUserLogicService.getOrCreate(companyUserDTO);

        assertEquals(companyUserDTOResult, companyUserDTO);
    }

    @Test
    @DisplayName("Should save and return the companyuser when the companyuser is not exist")
    void getOrCreateWhenCompanyUserIsNotExistThenSaveAndReturnTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");

        when(companyUserService.findByNameEmailAndCompany(companyUserDTO.getName(), companyUserDTO.getEmail(), companyUserDTO.getCompanyName())).thenReturn(null);
        when(companyUserService.save(companyUserDTO)).thenReturn(companyUserDTO);

        CompanyUserDTO result = companyUserLogicService.getOrCreate(companyUserDTO);

        assertEquals(companyUserDTO, result);
    }
}