package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.repository.DataSourceRepository;
import com.catenax.valueaddedservice.service.mapper.DataSourceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSourceServiceTest {

    @Mock
    private DataSourceRepository dataSourceRepository;

    @Mock
    private DataSourceMapper dataSourceMapper;

    @InjectMocks
    private DataSourceService dataSourceService;

    @Test
    @DisplayName("Should return empty when the id is not found")
    void partialUpdateWhenIdIsNotFoundThenReturnEmpty() {
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        when(dataSourceRepository.findById(any())).thenReturn(Optional.empty());

        Optional<DataSourceDTO> result = dataSourceService.partialUpdate(dataSourceDTO);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update the datasource when the id is found")
    void partialUpdateWhenIdIsFound() {
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("Test");
        DataSource dataSource = new DataSource();
        dataSource.setId(1L);
        dataSource.setDataSourceName("Test");

        when(dataSourceRepository.findById(any())).thenReturn(Optional.of(dataSource));
        when(dataSourceRepository.save(any())).thenReturn(dataSource);
        when(dataSourceMapper.toDto(dataSource)).thenReturn(dataSourceDTO);

        Optional<DataSourceDTO> result = dataSourceService.partialUpdate(dataSourceDTO);

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getDataSourceName());
    }

    @Test
    @DisplayName("Should return all data sources")
    void findAllShouldReturnAllDataSources() {
        DataSource dataSource = new DataSource();
        dataSource.setId(1L);
        dataSource.setDataSourceName("Test");
        dataSource.setType(Type.Custom);
        dataSource.setYearPublished(2020);
        dataSource.setFileName("Test");

        when(dataSourceRepository.findAll()).thenReturn(Collections.singletonList(dataSource));

        List<DataSourceDTO> result = dataSourceService.findAll();

        assertEquals(0, result.size());

    }

    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(dataSourceRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DataSourceDTO> result = dataSourceService.findOne(id);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should return the datasource when the id is valid")
    void findOneWhenIdIsValid() {
        Long id = 1L;
        DataSource dataSource = new DataSource();
        dataSource.setId(id);
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(id);

        when(dataSourceRepository.findById(id)).thenReturn(Optional.of(dataSource));
        when(dataSourceMapper.toDto(dataSource)).thenReturn(dataSourceDTO);

        Optional<DataSourceDTO> result = dataSourceService.findOne(id);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), id);
    }

    @Test
    @DisplayName("Should delete the datasource when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        dataSourceService.delete(id);
        verify(dataSourceRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return all DataSourcesValues")
    void findAllDataSources() {
        DataSource dataSource = new DataSource();
        dataSource.setId(1L);
        dataSource.setDataSourceName("TEST");


        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("TEST");

        Page<DataSource> dataSourceValueDTOS = new PageImpl<>(List.of(dataSource));

        when(dataSourceRepository.findAll(any(Pageable.class))).thenReturn(dataSourceValueDTOS);
        when(dataSourceMapper.toDto(any(DataSource.class))).thenReturn(dataSourceDTO);

        Page<DataSourceDTO> result = dataSourceService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }


}