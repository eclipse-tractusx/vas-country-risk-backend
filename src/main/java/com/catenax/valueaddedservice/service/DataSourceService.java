package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.repository.DataSourceRepository;
import com.catenax.valueaddedservice.service.mapper.DataSourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
public class DataSourceService {

    private final Logger log = LoggerFactory.getLogger(DataSourceService.class);

    private final DataSourceRepository dataSourceRepository;

    private final DataSourceMapper dataSourceMapper;

    public DataSourceService(DataSourceRepository dataSourceRepository, DataSourceMapper dataSourceMapper) {
        this.dataSourceRepository = dataSourceRepository;
        this.dataSourceMapper = dataSourceMapper;
    }


    //API to get Ratings by Year
    @Transactional(readOnly = true)
    public List<DataSourceDTO> findRatingsByYear(Integer year) {
        return dataSourceMapper.toDto(dataSourceRepository.findByYearPublished(year));
    }

    //API to get All Years
    @Transactional(readOnly = true)
    public List<Integer> findAllYears() {
        List<Integer> years = new ArrayList<>();
        years.addAll(dataSourceMapper.toDto(dataSourceRepository.findAll()).stream().map(DataSourceDTO::getYearPublished).collect(Collectors.toSet()));
        return years;
    }

    /**
     * Save a dataSource.
     *
     * @param dataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public DataSourceDTO save(DataSourceDTO dataSourceDTO) {
        log.debug("Request to save DataSource : {}", dataSourceDTO);
        DataSource dataSource = dataSourceMapper.toEntity(dataSourceDTO);
        dataSource = dataSourceRepository.save(dataSource);
        return dataSourceMapper.toDto(dataSource);
    }

    /**
     * Update a dataSource.
     *
     * @param dataSourceDTO the entity to save.
     * @return the persisted entity.
     */
    public DataSourceDTO update(DataSourceDTO dataSourceDTO) {
        log.debug("Request to save DataSource : {}", dataSourceDTO);
        DataSource dataSource = dataSourceMapper.toEntity(dataSourceDTO);
        dataSource = dataSourceRepository.save(dataSource);
        return dataSourceMapper.toDto(dataSource);
    }

    /**
     * Partially update a dataSource.
     *
     * @param dataSourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DataSourceDTO> partialUpdate(DataSourceDTO dataSourceDTO) {
        log.debug("Request to partially update DataSource : {}", dataSourceDTO);

        return dataSourceRepository
            .findById(dataSourceDTO.getId())
            .map(existingDataSource -> {
                dataSourceMapper.partialUpdate(existingDataSource, dataSourceDTO);

                return existingDataSource;
            })
            .map(dataSourceRepository::save)
            .map(dataSourceMapper::toDto);
    }

    /**
     * Get all the dataSources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DataSourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataSources");
        return dataSourceRepository.findAll(pageable).map(dataSourceMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<DataSourceDTO> findAll() {
        log.debug("Request to get all DataSources");
        return dataSourceMapper.toDto(dataSourceRepository.findAll());
    }

    /**
     * Get one dataSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DataSourceDTO> findOne(Long id) {
        log.debug("Request to get DataSource : {}", id);
        return dataSourceRepository.findById(id).map(dataSourceMapper::toDto);
    }

    /**
     * Delete the dataSource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DataSource : {}", id);
        dataSourceRepository.deleteById(id);
    }
}
