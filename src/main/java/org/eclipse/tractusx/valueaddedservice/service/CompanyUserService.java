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

import org.eclipse.tractusx.valueaddedservice.domain.CompanyUser;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyUserRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CompanyUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CompanyUser}.
 */
@Service
public class CompanyUserService {

    private final Logger log = LoggerFactory.getLogger(CompanyUserService.class);

    private final CompanyUserRepository companyUserRepository;

    private final CompanyUserMapper companyUserMapper;

    public CompanyUserService(CompanyUserRepository companyUserRepository, CompanyUserMapper companyUserMapper) {
        this.companyUserRepository = companyUserRepository;
        this.companyUserMapper = companyUserMapper;
    }

    /**
     * Save a companyUser.
     *
     * @param companyUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyUserDTO save(CompanyUserDTO companyUserDTO) {
        CompanyUser companyUser = companyUserMapper.toEntity(companyUserDTO);
        companyUser = companyUserRepository.save(companyUser);
        return companyUserMapper.toDto(companyUser);
    }

    public CompanyUserDTO findByNameEmailAndCompany(String companyUserName, String email, String companyName) {
        CompanyUser companyUser = companyUserRepository.findByNameAndEmailAndCompanyName(companyUserName, email, companyName);

        return companyUserMapper.toDto(companyUser);
    }

    public List<CompanyUserDTO> findAllUserFromCompany(String company) {
        return companyUserMapper.toDto(companyUserRepository.findByCompanyName(company));
    }

    /**
     * Partially update a companyUser.
     *
     * @param companyUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyUserDTO> partialUpdate(CompanyUserDTO companyUserDTO) {
        log.debug("Request to partially update CompanyUser : {}", companyUserDTO);

        return companyUserRepository
                .findById(companyUserDTO.getId())
                .map(existingCompanyUser -> {
                    companyUserMapper.partialUpdate(existingCompanyUser, companyUserDTO);

                    return existingCompanyUser;
                })
                .map(companyUserRepository::save)
                .map(companyUserMapper::toDto);
    }

    /**
     * Get all the companyUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    public Page<CompanyUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyUsers");
        return companyUserRepository.findAll(pageable).map(companyUserMapper::toDto);
    }

    /**
     * Get one companyUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<CompanyUserDTO> findOne(Long id) {
        log.debug("Request to get CompanyUser : {}", id);
        return companyUserRepository.findById(id).map(companyUserMapper::toDto);
    }

    /**
     * Delete the companyUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyUser : {}", id);
        companyUserRepository.deleteById(id);
    }
}
