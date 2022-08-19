package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.domain.Range;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.repository.RangeRepository;
import com.catenax.valueaddedservice.service.mapper.CompanyUserMapper;
import com.catenax.valueaddedservice.service.mapper.RangeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Range}.
 */
@Service
@Transactional
public class RangeService {

    private final Logger log = LoggerFactory.getLogger(RangeService.class);

    @Autowired
    CompanyUserMapper companyUserMapper;

    private final RangeRepository rangeRepository;

    private final RangeMapper rangeMapper;

    public RangeService(RangeRepository rangeRepository, RangeMapper rangeMapper) {
        this.rangeRepository = rangeRepository;
        this.rangeMapper = rangeMapper;
    }

    //API to get All Ranges Values by User
    @Transactional(readOnly = true)
    public List<RangeDTO> getUserRangesOrDefault(CompanyUserDTO companyUser) {

        List<RangeDTO> ranges = rangeMapper.toDto(rangeRepository.findByCompanyUser(companyUserMapper.toEntity(companyUser)));
        if(!ranges.isEmpty()){
            return ranges;
        }
        RangeDTO rangeDTOMin = new RangeDTO();
        rangeDTOMin.setRange(RangeType.Min);
        rangeDTOMin.setCompanyUser(companyUser);
        rangeDTOMin.setDescription("Min Range");
        rangeDTOMin.setValue(25);
        ranges.add(rangeDTOMin);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUser);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(50);
        ranges.add(rangeDTOBetWeen);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUser);
        rangeDTOMax.setDescription("Max Range");
        rangeDTOMax.setValue(100);
        ranges.add(rangeDTOMax);
        return ranges;
    }

    //API to get All Ranges by User [LIST]
    @Transactional(readOnly = true)
    public List<RangeDTO> getAllRangesList(CompanyUserDTO companyUser) {
        return rangeMapper.toDto(rangeRepository.findByCompanyUser(companyUserMapper.toEntity(companyUser)));
    }


    /**
     * Save a range.
     *
     * @param rangeDTO the entity to save.
     * @return the persisted entity.
     */
    public RangeDTO save(RangeDTO rangeDTO) {
        log.debug("Request to save Range : {}", rangeDTO);
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
        log.debug("Request to update Range for user : {}", rangeDTO);
        Range range = rangeMapper.toEntity(rangeDTO);
        rangeRepository.setValueForRange(range.getValue(),range.getRange(),range.getCompanyUser().getId());
    }

    /**
     * Update a range.
     *
     * @param rangeDTO the entity to save.
     * @return the persisted entity.
     */
    public RangeDTO update(RangeDTO rangeDTO) {
        log.debug("Request to save Range : {}", rangeDTO);
        Range range = rangeMapper.toEntity(rangeDTO);
        range = rangeRepository.save(range);
        return rangeMapper.toDto(range);
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
