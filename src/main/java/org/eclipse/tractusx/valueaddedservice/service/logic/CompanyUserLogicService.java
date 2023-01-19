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


import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Slf4j
public class CompanyUserLogicService {

    @Autowired
    CompanyUserService companyUserService;

    @Autowired
    ApplicationVariables applicationVariables;

    public CompanyUserDTO getOrCreate(CompanyUserDTO companyUserDTO)  {
        CompanyUserDTO companyUserDTOUse = companyUserService.findByNameEmailAndCompany(companyUserDTO.getName(), companyUserDTO.getEmail(), companyUserDTO.getCompanyName());
        if(companyUserDTOUse == null){
            companyUserDTOUse =companyUserService.save(companyUserDTO);
        }

        return companyUserDTOUse;
    }

    public CompanyUserDTO findByNameEmailAndCompany(String companyUserName, String email, String companyName)  {
        CompanyUserDTO companyUserDTOUse = companyUserService.findByNameEmailAndCompany(companyUserName,email,companyName);
        if(companyUserDTOUse == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return companyUserDTOUse;
    }

    public List<CompanyUserDTO> getUsersFromCompany(CompanyUserDTO companyUserDTO){
        return companyUserService.findAllUserFromCompany(companyUserDTO.getCompanyName());
    }

    public boolean validateUserAndTokenAreTheSame(CompanyUserDTO companyUserDTO){
        return companyUserDTO.getCompanyName().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getCompanyName()) &&
                companyUserDTO.getEmail().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getEmail()) &&
                companyUserDTO.getName().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getName());
    }

    public boolean isAdmin(){
        return applicationVariables.getAuthPropertiesDTO().getIsAdmin();
    }







}
