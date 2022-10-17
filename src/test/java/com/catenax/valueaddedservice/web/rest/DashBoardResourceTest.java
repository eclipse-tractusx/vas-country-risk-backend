package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.service.DashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dashboard Resource Tests")
class DashBoardResourceTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashBoardResource dashBoardResource;

    @Test
    @DisplayName("Should return an empty list when the report is null")
    void getReportsValueByReportWhenReportIsNull() {
        ReportDTO reportDTO = null;

        var result = dashBoardResource.getReportsValueByReport(reportDTO);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    @DisplayName("Should return a list of reportvaluesdto when the report is not null")
    void getReportsValueByReportWhenReportIsNotNull() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(1L);
        reportDTO.setReportName("test");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");

        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setId(1L);
        reportValuesDTO.setReport(reportDTO);

        when(dashboardService.getReportValues(reportDTO)).thenReturn(List.of(reportValuesDTO));

        var result = dashBoardResource.getReportsValueByReport(reportDTO);

        assertEquals(1, result.getBody().size());
    }
}