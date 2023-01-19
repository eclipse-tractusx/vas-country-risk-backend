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

import org.eclipse.tractusx.valueaddedservice.domain.CompanyGates;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyGatesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyGatesRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CompanyGatesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing {@link CompanyGates}.
 */
@Service
@Transactional
public class CompanyGatesService {

    private final Logger log = LoggerFactory.getLogger(CompanyGatesService.class);

    private final CompanyGatesRepository companyGatesRepository;

    private final CompanyGatesMapper companyGatesMapper;

    public CompanyGatesService(CompanyGatesRepository companyGatesRepository, CompanyGatesMapper companyGatesMapper) {
        this.companyGatesRepository = companyGatesRepository;
        this.companyGatesMapper = companyGatesMapper;
    }

    /**
     * Save a companyGates.
     *
     * @param companyGatesDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyGatesDTO save(CompanyGatesDTO companyGatesDTO) {
        log.debug("Request to save CompanyGates : {}", companyGatesDTO);
        CompanyGates companyGates = companyGatesMapper.toEntity(companyGatesDTO);
        companyGates = companyGatesRepository.save(companyGates);
        return companyGatesMapper.toDto(companyGates);
    }

    public List<CompanyGatesDTO> findByCompanyGroup(CompanyDTO companyDTO){
        return companyGatesMapper.toDto(companyGatesRepository.findByCompanyGroupId(companyDTO.getCompanyGroup().getId()));
    }




}
