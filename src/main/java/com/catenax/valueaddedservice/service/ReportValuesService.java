package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.domain.ReportValues;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportValuesRepository;
import com.catenax.valueaddedservice.service.mapper.ReportMapper;
import com.catenax.valueaddedservice.service.mapper.ReportValuesMapper;
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

    public ReportValuesService(ReportValuesRepository reportValuesRepository, ReportValuesMapper reportValuesMapper,ReportMapper reportMapper) {
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
