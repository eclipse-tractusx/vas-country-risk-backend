package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.RegionValue;
import com.catenax.valueaddedservice.dto.RegionValueDTO;
import com.catenax.valueaddedservice.repository.RegionValueRepository;
import com.catenax.valueaddedservice.service.mapper.RegionValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RegionValue}.
 */
@Service
@Transactional
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
     * Update a regionValue.
     *
     * @param regionValueDTO the entity to save.
     * @return the persisted entity.
     */
    public RegionValueDTO update(RegionValueDTO regionValueDTO) {
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
