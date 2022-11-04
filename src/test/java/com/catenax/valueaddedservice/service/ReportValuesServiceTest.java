package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.ReportValues;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportValuesRepository;
import com.catenax.valueaddedservice.service.mapper.ReportMapper;
import com.catenax.valueaddedservice.service.mapper.ReportValuesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportValuesServiceTest {

    @Mock
    private ReportValuesRepository reportValuesRepository;

    @Mock
    private ReportValuesMapper reportValuesMapper;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportValuesService reportValuesService;

    @Test
    @DisplayName("Should return all the reportvalues")
    void findAllShouldReturnAllReportValues() {
        ReportValues reportValues = new ReportValues();
        reportValues.setId(1L);
        reportValues.setName("Range");
        reportValues.setObjectValue("{\"min\":\"1\",\"max\":\"10\"}");

        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setId(1L);
        reportValuesDTO.setName("Range");
        reportValuesDTO.setObjectValue("{\"min\":\"1\",\"max\":\"10\"}");

        Page<ReportValues> reportValuesPage = new PageImpl<>(List.of(reportValues));

        when(reportValuesRepository.findAll(any(Pageable.class))).thenReturn(reportValuesPage);
        when(reportValuesMapper.toDto(any(ReportValues.class))).thenReturn(reportValuesDTO);

        Page<ReportValuesDTO> result = reportValuesService.findAll(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should delete the reportvalues when the id is valid")
    void deleteWhenIdIsValid() {
        Long id = 1L;
        reportValuesService.delete(id);
        verify(reportValuesRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should return empty when the id is invalid")
    void findOneWhenIdIsInvalid() {
        Long id = 1L;
        when(reportValuesRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ReportValuesDTO> reportValuesDTO = reportValuesService.findOne(id);

        assertFalse(reportValuesDTO.isPresent());
    }

    @Test
    @DisplayName("Should throw an exception when the report is null")
    void saveWhenReportIsNullThenThrowException() {
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        assertThrows(
                NullPointerException.class, () -> reportValuesService.save(reportValuesDTO, null));
    }
}