package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.CompanyGates;
import com.catenax.valueaddedservice.dto.CompanyDTO;
import com.catenax.valueaddedservice.dto.CompanyGatesDTO;
import com.catenax.valueaddedservice.repository.CompanyGatesRepository;
import com.catenax.valueaddedservice.service.mapper.CompanyGatesMapper;
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
