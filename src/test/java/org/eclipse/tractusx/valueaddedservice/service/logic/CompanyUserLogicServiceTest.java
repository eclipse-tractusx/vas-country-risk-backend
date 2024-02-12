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
import org.eclipse.tractusx.valueaddedservice.service.CompanyUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyUserLogicServiceTest {

    @Mock
    CompanyUserService companyUserService;

    @Mock
    ApplicationVariables applicationVariables;

    @InjectMocks
    CompanyUserLogicService companyUserLogicService;

    @Test
    @DisplayName("Should return true when the company name, email and name are the same")
    void validateUserAndTokenAreTheSameWhenCompanyNameEmailAndNameAreTheSameThenReturnTrue() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("TestCompany");
        companyUserDTO.setEmail("test@email.com");
        companyUserDTO.setName("TestName");

        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("TestCompany");
        authPropertiesDTO.setEmail("test@email.com");
        authPropertiesDTO.setName("TestName");

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);

        assertTrue(companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO));
    }

    @Test
    @DisplayName("Should return false when the company name, email and name are not the same")
    void validateUserAndTokenAreTheSameWhenCompanyNameEmailAndNameAreNotTheSameThenReturnFalse() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("companyName");
        companyUserDTO.setEmail("email");
        companyUserDTO.setName("name");

        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("companyName1");
        authPropertiesDTO.setEmail("email1");
        authPropertiesDTO.setName("name1");

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);

        boolean result = companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO);

        assertFalse(result);
    }
}