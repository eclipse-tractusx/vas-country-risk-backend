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

import org.eclipse.tractusx.valueaddedservice.domain.RegionValue;
import org.eclipse.tractusx.valueaddedservice.dto.RegionValueDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RegionValueRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.RegionValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RegionValue}.
 */
@Service
public class RegionValueService {

    private final Logger log = LoggerFactory.getLogger(RegionValueService.class);

    private final RegionValueRepository regionValueRepository;

    private final RegionValueMapper regionValueMapper;

    public RegionValueService(RegionValueRepository regionValueRepository, RegionValueMapper regionValueMapper) {
        this.regionValueRepository = regionValueRepository;
        this.regionValueMapper = regionValueMapper;
    }

    /**
     * Save a regionValue.
     *
     * @param regionValueDTO the entity to save.
     * @return the persisted entity.
     */
    public RegionValueDTO save(RegionValueDTO regionValueDTO) {
        log.debug("Request to save RegionValue : {}", regionValueDTO);
        RegionValue regionValue = regionValueMapper.toEntity(regionValueDTO);
        regionValue = regionValueRepository.save(regionValue);
        return regionValueMapper.toDto(regionValue);
    }



    /**
     * Partially update a regionValue.
     *
     * @param regionValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RegionValueDTO> partialUpdate(RegionValueDTO regionValueDTO) {
        log.debug("Request to partially update RegionValue : {}", regionValueDTO);

        return regionValueRepository
            .findById(regionValueDTO.getId())
            .map(existingRegionValue -> {
                regionValueMapper.partialUpdate(existingRegionValue, regionValueDTO);

                return existingRegionValue;
            })
            .map(regionValueRepository::save)
            .map(regionValueMapper::toDto);
    }

    /**
     * Get all the regionValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<RegionValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RegionValues");
        return regionValueRepository.findAll(pageable).map(regionValueMapper::toDto);
    }

    /**
     * Get one regionValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<RegionValueDTO> findOne(Long id) {
        log.debug("Request to get RegionValue : {}", id);
        return regionValueRepository.findById(id).map(regionValueMapper::toDto);
    }

    /**
     * Delete the regionValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RegionValue : {}", id);
        regionValueRepository.deleteById(id);
    }
}
