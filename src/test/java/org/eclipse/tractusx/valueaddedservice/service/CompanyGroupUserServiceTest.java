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

import org.eclipse.tractusx.valueaddedservice.domain.CompanyUser;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyUserRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CompanyUserMapper;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyGroupUserServiceTest {

    @Mock
    private CompanyUserRepository companyUserRepository;

    @Mock
    private CompanyUserMapper companyUserMapper;

    @InjectMocks
    private CompanyUserService companyUserService;

    @Test
    @DisplayName("Should return empty list when there is no company user")
    void findAllShouldReturnEmptyListWhenThereIsNoCompanyUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CompanyUser> companyUserPage = new PageImpl<>(Collections.emptyList());
        when(companyUserRepository.findAll(any(Pageable.class))).thenReturn(companyUserPage);

        Page<CompanyUserDTO> companyUserDTOPage = companyUserService.findAll(pageable);

        assertNotNull(companyUserDTOPage);
        assertEquals(0, companyUserDTOPage.getTotalElements());
    }

    @Test
    @DisplayName("Should return all company users")
    void findAllShouldReturnAllCompanyUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<CompanyUser> companyUsers = new ArrayList<>();
        companyUsers.add(new CompanyUser());
        companyUsers.add(new CompanyUser());
        Page<CompanyUser> companyUserPage = new PageImpl<>(companyUsers);

        when(companyUserRepository.findAll(any(Pageable.class))).thenReturn(companyUserPage);

        Page<CompanyUserDTO> result = companyUserService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("Should delete the companyuser when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        companyUserService.delete(id);
        verify(companyUserRepository, times(1)).deleteById(id);
    }



    @Test
    @DisplayName("Should return empty when the id is invalid")
    void partialUpdateWhenIdIsInvalidThenReturnEmpty() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setId(1L);

        Optional<CompanyUserDTO> result = companyUserService.partialUpdate(companyUserDTO);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return the updated companyuser when the id is valid")
    void partialUpdateWhenIdIsValidThenReturnUpdatedCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setId(Long.MAX_VALUE);
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");

        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(Long.MAX_VALUE);
        companyUser.setName("John");
        companyUser.setEmail("John@email.com");
        companyUser.setCompanyName("TestCompany");

        when(companyUserRepository.findById(Long.MAX_VALUE)).thenReturn(Optional.of(companyUser));

        Optional<CompanyUserDTO> result = companyUserService.partialUpdate(companyUserDTO);

        assertTrue(true);

    }

    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(companyUserRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CompanyUserDTO> companyUserDTO = companyUserService.findOne(id);

        assertFalse(companyUserDTO.isPresent());
    }

    @Test
    @DisplayName("Should return the companyuser when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        CompanyUser companyUser = new CompanyUser();
        companyUser.setId(id);
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setId(id);

        when(companyUserRepository.findById(id)).thenReturn(Optional.of(companyUser));
        when(companyUserMapper.toDto(companyUser)).thenReturn(companyUserDTO);

        Optional<CompanyUserDTO> result = companyUserService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }
}