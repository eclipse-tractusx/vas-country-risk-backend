package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.service.mapper.ReportMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportService reportService;

    @Test
    @DisplayName("Should return all reports")
    void findAllShouldReturnAllReports() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Report> rangeList = new ArrayList<>();
        rangeList.add(new Report());
        rangeList.add(new Report());
        Page<Report> rangePage = new PageImpl<>(rangeList);

        when(reportRepository.findAll(any(Pageable.class))).thenReturn(rangePage);

        Page<ReportDTO> result = reportService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
    }


}