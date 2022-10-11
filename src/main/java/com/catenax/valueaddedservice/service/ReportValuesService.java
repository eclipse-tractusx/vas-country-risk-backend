package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.ReportValues;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportValuesRepository;
import com.catenax.valueaddedservice.service.mapper.ReportValuesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ReportValues}.
 */
@Service
@Transactional
public class ReportValuesService {

    private final Logger log = LoggerFactory.getLogger(ReportValuesService.class);

    private final ReportValuesRepository reportValuesRepository;

    private final ReportValuesMapper reportValuesMapper;

    public ReportValuesService(ReportValuesRepository reportValuesRepository, ReportValuesMapper reportValuesMapper) {
        this.reportValuesRepository = reportValuesRepository;
        this.reportValuesMapper = reportValuesMapper;
    }

    /**
     * Save a reportValues.
     *
     * @param reportValuesDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportValuesDTO save(ReportValuesDTO reportValuesDTO) {
        log.debug("Request to save ReportValues : {}", reportValuesDTO);
        ReportValues reportValues = reportValuesMapper.toEntity(reportValuesDTO);
        reportValues = reportValuesRepository.save(reportValues);
        return reportValuesMapper.toDto(reportValues);
    }

    /**
     * Update a reportValues.
     *
     * @param reportValuesDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportValuesDTO update(ReportValuesDTO reportValuesDTO) {
        log.debug("Request to save ReportValues : {}", reportValuesDTO);
        ReportValues reportValues = reportValuesMapper.toEntity(reportValuesDTO);
        reportValues = reportValuesRepository.save(reportValues);
        return reportValuesMapper.toDto(reportValues);
    }

    /**
     * Partially update a reportValues.
     *
     * @param reportValuesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportValuesDTO> partialUpdate(ReportValuesDTO reportValuesDTO) {
        log.debug("Request to partially update ReportValues : {}", reportValuesDTO);

        return reportValuesRepository
            .findById(reportValuesDTO.getId())
            .map(existingReportValues -> {
                reportValuesMapper.partialUpdate(existingReportValues, reportValuesDTO);

                return existingReportValues;
            })
            .map(reportValuesRepository::save)
            .map(reportValuesMapper::toDto);
    }

    /**
     * Get all the reportValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportValuesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReportValues");
        return reportValuesRepository.findAll(pageable).map(reportValuesMapper::toDto);
    }

    /**
     * Get one reportValues by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
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
