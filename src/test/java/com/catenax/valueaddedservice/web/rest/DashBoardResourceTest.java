package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.service.DashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dashboard Resource Tests")
class DashBoardResourceTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashBoardResource dashBoardResource;



    @Test
    @DisplayName("Should return a list of dashboardworldmapdto when the year is not null")
    void getDashBoardWorldMapWhenYearIsNotNullThenReturnListOfDashBoardWorldMapDTO() throws IOException {
        Integer year = 2020;
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("company");
        companyUserDTO.setName("name");
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS = List.of(new DashBoardWorldMapDTO());
        when(dashboardService.getWorldMapInfo(anyInt(), anyList(), any()))
                .thenReturn(dashBoardWorldMapDTOS);

        List<DashBoardWorldMapDTO> result =
                dashBoardResource.getDashBoardWorldMap(null, year, companyUserDTO).getBody();

        assertEquals(dashBoardWorldMapDTOS, result);
    }



    @Test
    @DisplayName("Should return a list of dashboardtabledto when the year is not null")
    void getAllDashBoardTableWhenYearIsNotNull() throws IOException {
        ListRatingDTO listRatingDTO = new ListRatingDTO();
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("company");
        companyUserDTO.setName("name");
        when(dashboardService.getTableInfo(anyInt(), anyList(), any()))
                .thenReturn(List.of(new DashBoardTableDTO()));
        var result = dashBoardResource.getAllDashBoardTable(listRatingDTO, 2020, companyUserDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }



    @Test
    @DisplayName("Should return a csv file")
    void getTemplateShouldReturnCSVFile() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName("test");
        fileDTO.setContent("test");
        when(dashboardService.getDataSourceTemplate()).thenReturn(fileDTO);
        assertEquals(dashBoardResource.getTemplate().getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Should return an empty list when the user is not authenticated")
    void getBpnCountrysWhenUserIsNotAuthenticatedThenReturnEmptyList() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        when(dashboardService.getCountryByAssociatedBPtoUser(any())).thenReturn(List.of());
        List<CountryDTO> countryDTOS = dashBoardResource.getBpnCountrys(companyUserDTO).getBody();
        assertEquals(0, countryDTOS.size());
    }

    @Test
    @DisplayName("Should return a list of countries when the user is authenticated")
    void getBpnCountrysWhenUserIsAuthenticatedThenReturnListOfCountries() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("company");
        companyUserDTO.setName("name");
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setIso2("iso2");
        countryDTO.setCountry("name");
        when(dashboardService.getCountryByAssociatedBPtoUser(any(CompanyUserDTO.class)))
                .thenReturn(List.of(countryDTO));

        List<CountryDTO> countryDTOS = dashBoardResource.getBpnCountrys(companyUserDTO).getBody();

        assertNotNull(countryDTOS);
        assertEquals(1, countryDTOS.size());
        assertEquals("iso2", countryDTOS.get(0).getIso2());
        assertEquals("name", countryDTOS.get(0).getCountry());
    }

    @Test
    @DisplayName("Should return all business partners of a company")
    void getCompanyBpnsShouldReturnAllBusinessPartnersOfACompany() {
        dashBoardResource.getCompanyBpns(null);
        verify(dashboardService, times(1)).getExternalBusinessPartners(null);
    }

    @Test
    @DisplayName("Should save the report when the report is valid")
    void saveReportsWhenReportIsValid() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("test");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");
        doNothing().when(dashboardService).saveReportForUser(any(), any());
        dashBoardResource.saveReports(reportDTO, null);
        verify(dashboardService, times(1)).saveReportForUser(any(), any());
    }

    @Test
    @DisplayName("Should return all reports of a company user")
    void getReportsByCompanyUserShouldReturnAllReportsOfACompanyUser() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(1L);
        reportDTO.setReportName("Report 1");
        reportDTO.setType(Type.Global);

        ReportDTO reportDTO2 = new ReportDTO();
        reportDTO2.setId(2L);
        reportDTO2.setReportName("Report 2");
        reportDTO2.setType(Type.Company);

        ReportDTO reportDTO3 = new ReportDTO();
        reportDTO3.setId(3L);
        reportDTO3.setReportName("Report 3");
        reportDTO3.setType(Type.Custom);

        when(dashboardService.getReportsByCompanyUser(any()))
                .thenReturn(List.of(reportDTO, reportDTO2, reportDTO3));

        List<ReportDTO> reports = dashBoardResource.getReportsByCompanyUser(any()).getBody();

        assertEquals(3, reports.size());
    }

    @Test
    @DisplayName("Should save the ranges when the ranges are valid")
    void saveRangesWhenRangesAreValid() {
        List<RangeDTO> rangeDTOS = List.of(new RangeDTO());
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        doNothing().when(dashboardService).saveRanges(rangeDTOS, companyUserDTO);

        dashBoardResource.saveRanges(rangeDTOS, companyUserDTO);

        verify(dashboardService, times(1)).saveRanges(rangeDTOS, companyUserDTO);
    }

    @Test
    @DisplayName("Should save the file when the file is not empty")
    void uploadFileWhenFileIsNotEmpty() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("test");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setReport(reportDTO);
        List<ReportValuesDTO> reportValuesDTOS = List.of(reportValuesDTO);

        when(dashboardService.getReportValues(reportDTO)).thenReturn(reportValuesDTOS);

        List<ReportValuesDTO> result = dashBoardResource.getReportsValueByReport(reportDTO).getBody();

        assertEquals(reportValuesDTOS, result);
    }

    @Test
    @DisplayName("Should throw an exception when the file is empty")
    void uploadFileWhenFileIsEmptyThenThrowException() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setType(Type.Company);
        reportDTO.setReportName("test");
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setReport(reportDTO);
        List<ReportValuesDTO> reportValuesDTOS = List.of(reportValuesDTO);
        when(dashboardService.getReportValues(reportDTO)).thenReturn(reportValuesDTOS);

        List<ReportValuesDTO> result = dashBoardResource.getReportsValueByReport(reportDTO).getBody();

        assertEquals(reportValuesDTOS, result);
    }

    @Test
    @DisplayName("Should return a list of countries when the user is authenticated")
    void getCountrysWhenUserIsAuthenticatedThenReturnListOfCountries() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("company");
        companyUserDTO.setName("name");
        companyUserDTO.setEmail("email");
        companyUserDTO.setId(1L);

        when(dashboardService.getCountryFilterByISO2(companyUserDTO)).thenReturn(List.of());

        var result = dashBoardResource.getCountrys(companyUserDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("Should return the ranges of the current user")
    void userRangesShouldReturnTheRangesOfTheCurrentUser() {
        dashBoardResource.userRanges(null);
        verify(dashboardService, times(1)).getUserRangesOrDefault(null);
    }

    @Test
    @DisplayName("Should return a list of datasourcedto when the year is not null")
    void ratingsByYearWhenYearIsNotNullThenReturnListOfDataSourceDTO() {
        Integer year = 2020;
        List<DataSourceDTO> dataSourceDTOS = List.of(new DataSourceDTO());
        when(dashboardService.findRatingsByYearAndCompanyUser(year, null))
                .thenReturn(dataSourceDTOS);
        var result = dashBoardResource.ratingsByYear(year, null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dataSourceDTOS, result.getBody());
    }

    @Test
    @DisplayName("Should return a list of datasourcedto when the year is null")
    void ratingsByYearWhenYearIsNullThenReturnListOfDataSourceDTO() {
        Integer year = null;
        List<DataSourceDTO> dataSourceDTOS = List.of(new DataSourceDTO());
        when(dashboardService.findRatingsByYearAndCompanyUser(year, null))
                .thenReturn(dataSourceDTOS);
        var result = dashBoardResource.ratingsByYear(year, null);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dataSourceDTOS, result.getBody());
    }

    @Test
    @DisplayName("Should return all years of the user ratings")
    void getYearsShouldReturnAllYearsOfTheUserRatings() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompany("company");
        companyUserDTO.setName("name");
        List<Integer> years = List.of(2020, 2019);
        when(dashboardService.getYearsOfUserRatings(companyUserDTO)).thenReturn(years);

        List<Integer> result = dashBoardResource.getYears(companyUserDTO).getBody();

        assertEquals(years, result);
    }

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