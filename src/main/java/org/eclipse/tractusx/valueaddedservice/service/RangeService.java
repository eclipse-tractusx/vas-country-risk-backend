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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.domain.Range;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.RangeDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RangeRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CompanyUserMapper;
import org.eclipse.tractusx.valueaddedservice.service.mapper.RangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Range}.
 */
@Service
@Slf4j
public class RangeService {

    @Autowired
    CompanyUserMapper companyUserMapper;

    @Autowired
    CompanyUserService companyUserService;

    private final RangeRepository rangeRepository;

    private final RangeMapper rangeMapper;



    public RangeService(RangeRepository rangeRepository, RangeMapper rangeMapper) {
        this.rangeRepository = rangeRepository;
        this.rangeMapper = rangeMapper;
    }

    //API to get All Ranges Values by User
    public List<RangeDTO> getUserRanges(CompanyUserDTO companyUser) {
        return rangeMapper.toDto(rangeRepository.findByCompanyUserNameAndCompanyUserEmailAndCompanyUserCompanyName(companyUser.getName(), companyUser.getEmail(), companyUser.getCompanyName()));
    }



    /**
     * Save a range.
     *
     * @param rangeDTO the entity to save.
     * @return the persisted entity.
     */
    public RangeDTO save(RangeDTO rangeDTO) {
        log.debug("Request to save Range");
        Range range = rangeMapper.toEntity(rangeDTO);
        range = rangeRepository.save(range);
        return rangeMapper.toDto(range);
    }

    /**
     * Save a range.
     *
     * @param rangeDTO the entity to save.
     * @return the persisted entity.
     */
    public void updateRanges(RangeDTO rangeDTO) {
        log.debug("Request to update Range for user");
        Range range = rangeMapper.toEntity(rangeDTO);
        rangeRepository.setValueForRange(range.getValue(),range.getRange(),range.getCompanyUser().getId());
    }


    /**
     * Partially update a range.
     *
     * @param rangeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RangeDTO> partialUpdate(RangeDTO rangeDTO) {
        log.debug("Request to partially update Range : {}", rangeDTO);

        return rangeRepository
                .findById(rangeDTO.getId())
                .map(existingRange -> {
                    rangeMapper.partialUpdate(existingRange, rangeDTO);

                    return existingRange;
                })
                .map(rangeRepository::save)
                .map(rangeMapper::toDto);
    }

    /**
     * Get all the ranges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<RangeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ranges");
        return rangeRepository.findAll(pageable).map(rangeMapper::toDto);
    }

    /**
     * Get one range by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<RangeDTO> findOne(Long id) {
        log.debug("Request to get Range : {}", id);
        return rangeRepository.findById(id).map(rangeMapper::toDto);
    }

    /**
     * Delete the range by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Range : {}", id);
        rangeRepository.deleteById(id);
    }
}
