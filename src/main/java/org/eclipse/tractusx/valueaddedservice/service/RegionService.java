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

import org.eclipse.tractusx.valueaddedservice.domain.Region;
import org.eclipse.tractusx.valueaddedservice.dto.RegionDTO;
import org.eclipse.tractusx.valueaddedservice.repository.RegionRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Region}.
 */
@Service
public class RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionService.class);

    private final RegionRepository regionRepository;

    private final RegionMapper regionMapper;

    public RegionService(RegionRepository regionRepository, RegionMapper regionMapper) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    /**
     * Save a region.
     *
     * @param regionDTO the entity to save.
     * @return the persisted entity.
     */
    public RegionDTO save(RegionDTO regionDTO) {
        log.debug("Request to save Region : {}", regionDTO);
        Region region = regionMapper.toEntity(regionDTO);
        region = regionRepository.save(region);
        return regionMapper.toDto(region);
    }



    /**
     * Partially update a region.
     *
     * @param regionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RegionDTO> partialUpdate(RegionDTO regionDTO) {
        log.debug("Request to partially update Region : {}", regionDTO);

        return regionRepository
            .findById(regionDTO.getId())
            .map(existingRegion -> {
                regionMapper.partialUpdate(existingRegion, regionDTO);

                return existingRegion;
            })
            .map(regionRepository::save)
            .map(regionMapper::toDto);
    }

    /**
     * Get all the regions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<RegionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Regions");
        return regionRepository.findAll(pageable).map(regionMapper::toDto);
    }

    /**
     * Get one region by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<RegionDTO> findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        return regionRepository.findById(id).map(regionMapper::toDto);
    }

    /**
     * Delete the region by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
    }
}
