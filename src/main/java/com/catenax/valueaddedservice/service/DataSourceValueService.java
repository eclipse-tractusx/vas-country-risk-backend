package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DashBoardDTO;
import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import com.catenax.valueaddedservice.repository.DataSourceValueRepository;
import com.catenax.valueaddedservice.service.mapper.DataSourceValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link DataSourceValue}.
 */
@Service
@Transactional
public class DataSourceValueService {

    private final Logger log = LoggerFactory.getLogger(DataSourceValueService.class);

    private final DataSourceValueRepository dataSourceValueRepository;

    private final DataSourceValueMapper dataSourceValueMapper;

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
     * Update a dataSourceValue.
     *
     * @param dataSourceValueDTO the entity to save.
     * @return the persisted entity.
     */
    public DataSourceValueDTO update(DataSourceValueDTO dataSourceValueDTO) {
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
    @Transactional(readOnly = true)
    public Page<DataSourceValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataSourceValues");
        return dataSourceValueRepository.findAll(pageable).map(dataSourceValueMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<DashBoardDTO> findByScoreGreaterThan(Float number) {
        log.debug("Request to get all DataSourceValues with score more than {}",number);
        return dataSourceValueRepository.findByScoreGreaterThan(number);
    }

    /**
     * Get one dataSourceValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
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
