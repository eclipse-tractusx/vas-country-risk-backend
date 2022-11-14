package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DataDTO;
import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import com.catenax.valueaddedservice.repository.DataSourceValueRepository;
import com.catenax.valueaddedservice.service.mapper.DataSourceValueMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSourceValueServiceTest {

    @Mock
    private DataSourceValueRepository dataSourceValueRepository;

    @Mock
    private DataSourceValueMapper dataSourceValueMapper;

    @InjectMocks
    private DataSourceValueService dataSourceValueService;


    @Test
    @DisplayName("Should update the datasourcevalue when the id is found")
    void partialUpdateWhenIdIsFound() {
        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        dataSourceValueDTO.setId(1L);
        dataSourceValueDTO.setScore(10F);
        DataSourceValue dataSourceValue = new DataSourceValue();
        dataSourceValue.setId(1L);
        dataSourceValue.setScore(10F);

        when(dataSourceValueRepository.findById(any())).thenReturn(Optional.of(dataSourceValue));
        when(dataSourceValueRepository.save(any())).thenReturn(dataSourceValue);
        when(dataSourceValueMapper.toDto(dataSourceValue)).thenReturn(dataSourceValueDTO);

        Optional<DataSourceValueDTO> result = dataSourceValueService.partialUpdate(dataSourceValueDTO);

        assertTrue(result.isPresent());
        assertEquals(10F, result.get().getScore());
    }


    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(dataSourceValueRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DataSourceValueDTO> result = dataSourceValueService.findOne(id);

        assertFalse(result.isPresent());
    }


    @Test
    @DisplayName("Should delete the DataSourcesValues when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        dataSourceValueService.delete(id);
        verify(dataSourceValueRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return all DataSourcesValues")
    void findAllDataSourcesValues() {
        DataSourceValue dataSourceValue = new DataSourceValue();
        dataSourceValue.setId(1L);
        dataSourceValue.setScore(10F);


        DataSourceValueDTO dataSourceValueDTO = new DataSourceValueDTO();
        dataSourceValueDTO.setId(1L);
        dataSourceValueDTO.setScore(10F);

        Page<DataSourceValue> dataSourceValueDTOS = new PageImpl<>(List.of(dataSourceValue));

        when(dataSourceValueRepository.findAll(any(Pageable.class))).thenReturn(dataSourceValueDTOS);
        when(dataSourceValueMapper.toDto(any(DataSourceValue.class))).thenReturn(dataSourceValueDTO);

        Page<DataSourceValueDTO> result = dataSourceValueService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should return a list of dataDto filtered by iso2")
    void findByRatingAndCountryAndScoreGreaterThan() {
        DataDTO dataDTO = new DataDTO("PORTUGAL", 10F, "TESTE", "TET", "TE", "TESTE");
        List<DataDTO> dataDTOList = Arrays.asList(dataDTO);
        when(dataSourceValueRepository.findByRatingAndCountryAndScoreGreaterThan(10F, new ArrayList<>(), new ArrayList<>()))
                .thenReturn(dataDTOList);

        List<DataDTO> result = dataSourceValueService.findByRatingAndCountryAndScoreGreaterThan(10F, new ArrayList<>(), new ArrayList<>());

        assertEquals(1, result.size());
        assertEquals("PORTUGAL", result.get(0).getCountry());
    }

    @Test
    @DisplayName("Should return a list of dataDto")
    void findByRatingAndScoreGreaterThan() {
        DataDTO dataDTO = new DataDTO("PORTUGAL", 10F, "TESTE", "TET", "TE", "TESTE");
        List<DataDTO> dataDTOList = Arrays.asList(dataDTO);
        when(dataSourceValueRepository.findByRatingAndScoreGreaterThan(new ArrayList<>(),10F))
                .thenReturn(dataDTOList);

        List<DataDTO> result = dataSourceValueService.findByRatingAndScoreGreaterThan(10F, new ArrayList<>());

        assertEquals(1, result.size());
        assertEquals("PORTUGAL", result.get(0).getCountry());
    }

}