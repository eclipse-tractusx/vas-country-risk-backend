package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.service.mapper.ReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Report}.
 */
@Service

public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
    }

    /**
     * Save a report.
     *
     * @param reportDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportDTO save(ReportDTO reportDTO) {
        log.debug("Request to save Report : {}", reportDTO);
        Report report = reportMapper.toEntity(reportDTO);
        report = reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    /**
     * Get all the reports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    public Page<ReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable).map(reportMapper::toDto);
    }

    /**
     * Get one report by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<ReportDTO> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id).map(reportMapper::toDto);
    }

    /**
     * Delete the report by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
    }

    public List<ReportDTO> findByCompanyUserNameAndCompanyAndType(String name,String company, Type type){
        return reportMapper.toDto(reportRepository.findByCompanyUserNameAndCompanyAndType(name,company,type));
    }

    public List<ReportDTO> findByGlobalType(Type type){
        return reportMapper.toDto(reportRepository.findByType(type));
    }

    public List<ReportDTO> findByCompanyAndType(String company, Type type){
        return reportMapper.toDto(reportRepository.findByCompanyAndType(company,type));
    }
}
