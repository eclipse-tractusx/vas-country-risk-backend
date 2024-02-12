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

import org.eclipse.tractusx.valueaddedservice.domain.DataSourceValue;
import org.eclipse.tractusx.valueaddedservice.dto.DataDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceValueDTO;
import org.eclipse.tractusx.valueaddedservice.repository.DataSourceValueRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.DataSourceMapper;
import org.eclipse.tractusx.valueaddedservice.service.mapper.DataSourceValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DataSourceValue}.
 */
@Service
public class DataSourceValueService {

    private final Logger log = LoggerFactory.getLogger(DataSourceValueService.class);

    private final DataSourceValueRepository dataSourceValueRepository;

    private final DataSourceValueMapper dataSourceValueMapper;

    @Autowired
    DataSourceMapper dataSourceMapper;

    public DataSourceValueService(DataSourceValueRepository dataSourceValueRepository, DataSourceValueMapper dataSourceValueMapper) {
        this.dataSourceValueRepository = dataSourceValueRepository;
        this.dataSourceValueMapper = dataSourceValueMapper;
    }

    /**
     * Save a dataSourceValue.
     *
     * @param dataSourceValueDTO the entity to save.
     * @return the persisted entity.
     */
    public DataSourceValueDTO save(DataSourceValueDTO dataSourceValueDTO) {
        log.debug("Request to save DataSourceValue : {}", dataSourceValueDTO);
        DataSourceValue dataSourceValue = dataSourceValueMapper.toEntity(dataSourceValueDTO);
        dataSourceValue = dataSourceValueRepository.save(dataSourceValue);
        return dataSourceValueMapper.toDto(dataSourceValue);
    }



    /**
     * Partially update a dataSourceValue.
     *
     * @param dataSourceValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DataSourceValueDTO> partialUpdate(DataSourceValueDTO dataSourceValueDTO) {
        log.debug("Request to partially update DataSourceValue : {}", dataSourceValueDTO);

        return dataSourceValueRepository
            .findById(dataSourceValueDTO.getId())
            .map(existingDataSourceValue -> {
                dataSourceValueMapper.partialUpdate(existingDataSourceValue, dataSourceValueDTO);

                return existingDataSourceValue;
            })
            .map(dataSourceValueRepository::save)
            .map(dataSourceValueMapper::toDto);
    }

    /**
     * Get all the dataSourceValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    public Page<DataSourceValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataSourceValues");
        return dataSourceValueRepository.findAll(pageable).map(dataSourceValueMapper::toDto);
    }


    public List<DataDTO> findByRatingAndCountryAndScoreGreaterThanAndYear(Float score, List<String> country, List<String> dataSources, Integer year) {
        log.debug("Request to get all DataSourceValues with score {} in {} in {} and year {}",score,country,dataSources,year);
        return dataSourceValueRepository.findByRatingAndCountryAndScoreGreaterThanAndYear(score,country,dataSources,year);
    }


    public List<DataDTO> findByRatingAndCountryAndScoreGreaterThan(Float score, List<String> country,List<String> dataSources) {
        log.debug("Request to get all DataSourceValues with score {} in {} in {} ",score,country,dataSources);
        return dataSourceValueRepository.findByRatingAndCountryAndScoreGreaterThan(score,country,dataSources);
    }

    public List<DataDTO> findByRatingAndScoreGreaterThanAndYear(Float score, List<String> dataSources,Integer year) {
        log.debug("Request to get all DataSourceValues with score {} in dataSource {} and year {} ",score,dataSources,year);
        return dataSourceValueRepository.findByRatingAndScoreGreaterThanAndYear(dataSources,year,score);
    }


    public List<DataDTO> findByRatingAndScoreGreaterThan(Float score, List<String> dataSources) {
        log.debug("Request to get all DataSourceValues with score {} in dataSource {}",score,dataSources);
        return dataSourceValueRepository.findByRatingAndScoreGreaterThan(dataSources,score);
    }

    public List<DataSourceValueDTO> findByDataSource(DataSourceDTO dataSourceDTO) {
        log.debug("Request to get all DataSourceValues in dataSource {}",dataSourceDTO.getDataSourceName());
        return dataSourceValueMapper.toDto(dataSourceValueRepository.findByDataSource(dataSourceMapper.toEntity(dataSourceDTO)));
    }

    /**
     * Get one dataSourceValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<DataSourceValueDTO> findOne(Long id) {
        log.debug("Request to get DataSourceValue : {}", id);
        return dataSourceValueRepository.findById(id).map(dataSourceValueMapper::toDto);
    }

    /**
     * Delete the dataSourceValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataSourceValue : {}", id);
        dataSourceValueRepository.deleteById(id);
    }

}
