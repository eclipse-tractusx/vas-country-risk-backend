package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.service.ReportService;
import com.catenax.valueaddedservice.service.ReportValuesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportLogicService")
class ReportLogicServiceTest {

    @Mock
    ReportService reportService;

    @Mock
    ReportValuesService reportValuesService;

    @Mock
    ReportRepository reportRepository;

    @InjectMocks
    ReportLogicService reportLogicService;

    CompanyUserDTO companyUserDTO = new CompanyUserDTO(1L, "John", "john@gmail.com", "TestCompany");

    @Test
    @DisplayName("Should throw an exception when the report is null")
    void saveReportWhenReportIsNullThenThrowException() {
        assertThrows(
                NullPointerException.class,
                () -> reportLogicService.saveReport(null, companyUserDTO));
    }

    @Test
    @DisplayName("Should save the report when the report is not null")
    void saveReportWhenReportIsNotNull() {
        ReportDTO reportDTO = new ReportDTO(1L, "Fake Rating", "John", "Test Company", Type.Custom,new ArrayList<>());
        reportDTO.setReportValuesDTOList(
                Arrays.asList(new ReportValuesDTO(1L, "Range", "Test Range", null)));
        when(reportService.save(reportDTO)).thenReturn(reportDTO);
        reportLogicService.saveReport(reportDTO, companyUserDTO);
        verify(reportService, times(1)).save(reportDTO);
    }

    @Test
    @DisplayName("Should return an empty list when the report is null")
    void getReportValuesWhenReportIsNullThenReturnEmptyList() {
        ReportDTO reportDTO = null;
        List<ReportValuesDTO> reportValuesDTOList = reportLogicService.getReportValues(reportDTO);
        assertTrue(reportValuesDTOList.isEmpty());
    }

    @Test
    @DisplayName("Should return the report values when the report is not null")
    void getReportValuesWhenReportIsNotNull() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(1L);
        List<ReportValuesDTO> reportValuesDTOList = new ArrayList<>();
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setId(1L);
        reportValuesDTOList.add(reportValuesDTO);
        when(reportValuesService.findByReport(reportDTO)).thenReturn(reportValuesDTOList);
        List<ReportValuesDTO> result = reportLogicService.getReportValues(reportDTO);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return an empty list when there are no reports for the company user")
    void getReportsForCompanyUserShouldReturnAnEmptyListWhenThereAreNoReportsForTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");

        when(reportService.findByCompanyUserNameAndCompanyAndType(
                companyUserDTO.getName(), companyUserDTO.getCompany(), Type.Custom))
                .thenReturn(Collections.emptyList());

        List<ReportDTO> reports = reportLogicService.getReportsForCompanyUser(companyUserDTO);

        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }

    @Test
    @DisplayName("Should return the reports for the company user")
    void getReportsForCompanyUserShouldReturnTheReportsForTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(1L);
        reportDTO.setReportName("Fake Rating");
        reportDTO.setCompanyUserName("John");
        reportDTO.setCompany("Test Company");
        reportDTO.setType(Type.Custom);

        List<ReportValuesDTO> reportValuesDTOList = new ArrayList<>();
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setId(1L);
        reportValuesDTO.setName("Range");
        reportValuesDTOList.add(reportValuesDTO);

        reportDTO.setReportValuesDTOList(reportValuesDTOList);

        List<ReportDTO> reports = Collections.singletonList(reportDTO);

        when(reportService.findByCompanyUserNameAndCompanyAndType(
                companyUserDTO.getName(), companyUserDTO.getCompany(), Type.Custom))
                .thenReturn(reports);

        List<ReportDTO> result = reportLogicService.getReportsForCompanyUser(companyUserDTO);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return the reports for the company")
    void getCompanyReportsShouldReturnTheReportsForTheCompany() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("Test Company");
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setCompany(companyUserDTO.getCompany());
        List<ReportDTO> reportDTOList = Arrays.asList(reportDTO);
        when(reportService.findByCompanyAndType(companyUserDTO.getCompany(), Type.Company))
                .thenReturn(reportDTOList);

        List<ReportDTO> result = reportLogicService.getCompanyReports(companyUserDTO);

        assertEquals(reportDTOList, result);
    }


}