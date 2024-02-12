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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyGatesDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.service.CompanyGatesService;
import org.eclipse.tractusx.valueaddedservice.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CompanyGatesLogicService {

    @Autowired
    CompanyService companyUserLogicService;

    @Autowired
    CompanyGatesService companyGatesService;

    public List<CompanyGatesDTO> getGatesForCompanyUser(CompanyUserDTO companyUserDTO){
        Optional<CompanyDTO> companyDTOOptional = companyUserLogicService.getCompanyByCompanyName(companyUserDTO.getCompanyName());
        if(companyDTOOptional.isPresent()){
           List<CompanyGatesDTO> companyGatesDTOS = companyGatesService.findByCompanyGroup(companyDTOOptional.get());
           return companyGatesDTOS;
        }
        return new ArrayList<>();
    }




}
