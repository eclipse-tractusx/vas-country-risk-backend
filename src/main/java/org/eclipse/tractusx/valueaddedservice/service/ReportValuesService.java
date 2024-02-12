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

import org.eclipse.tractusx.valueaddedservice.domain.Report;
import org.eclipse.tractusx.valueaddedservice.domain.ReportValues;
import org.eclipse.tractusx.valueaddedservice.dto.ReportDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ReportValuesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.ReportValuesRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.ReportMapper;
import org.eclipse.tractusx.valueaddedservice.service.mapper.ReportValuesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ReportValues}.
 */
@Service
public class ReportValuesService {

    private final Logger log = LoggerFactory.getLogger(ReportValuesService.class);

    private final ReportValuesRepository reportValuesRepository;

    private final ReportValuesMapper reportValuesMapper;

    private final ReportMapper reportMapper;

    public ReportValuesService(ReportValuesRepository reportValuesRepository, ReportValuesMapper reportValuesMapper, ReportMapper reportMapper) {
        this.reportValuesRepository = reportValuesRepository;
        this.reportValuesMapper = reportValuesMapper;
        this.reportMapper = reportMapper;
    }

    /**
     * Save a reportValues.
     *
     * @param reportValuesDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportValuesDTO save(ReportValuesDTO reportValuesDTO, ReportDTO reportDTO) {
        log.debug("Request to save ReportValues : {}", reportValuesDTO);
        Report report = reportMapper.toEntity(reportDTO);
        ReportValues reportValues = reportValuesMapper.toEntity(reportValuesDTO);
        reportValues.setReport(report);
        reportValues = reportValuesRepository.save(reportValues);
        return reportValuesMapper.toDto(reportValues);
    }


    /**
     * Get all the reportValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    public Page<ReportValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportValues");
        return reportValuesRepository.findAll(pageable).map(reportValuesMapper::toDto);
    }


    public List<ReportValuesDTO> findByReport(ReportDTO reportDTO) {
        Report report = reportMapper.toEntity(reportDTO);
        log.debug("Request to get all ReportValues");
        return reportValuesMapper.toDto(reportValuesRepository.findByReport(report));
    }

    /**
     * Get one reportValues by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<ReportValuesDTO> findOne(Long id) {
        log.debug("Request to get ReportValues : {}", id);
        return reportValuesRepository.findById(id).map(reportValuesMapper::toDto);
    }

    /**
     * Delete the reportValues by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReportValues : {}", id);
        reportValuesRepository.deleteById(id);
    }
}
