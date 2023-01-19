/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.rest;

import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.*;
import org.eclipse.tractusx.valueaddedservice.service.DashboardService;
import org.eclipse.tractusx.valueaddedservice.web.rest.DashBoardResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
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
        companyUserDTO.setCompanyName("company");
        companyUserDTO.setName("name");
        List<DashBoardWorldMapDTO> dashBoardWorldMapDTOS = List.of(new DashBoardWorldMapDTO());
        when(dashboardService.getWorldMapInfo(anyInt(), anyList(), any()))
                .thenReturn(dashBoardWorldMapDTOS);

        List<DashBoardWorldMapDTO> result =
                dashBoardResource.getDashBoardWorldMap(new ArrayList<>(),companyUserDTO,year).getBody();

        assertEquals(dashBoardWorldMapDTOS, result);
    }



    @Test
    @DisplayName("Should return a list of dashboardtabledto when the year is not null")
    void getAllDashBoardTableWhenYearIsNotNull() throws IOException {
        List<RatingDTO> ratingDTOS= new ArrayList<>();
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("company");
        companyUserDTO.setName("name");
        when(dashboardService.getTableInfo(anyInt(), anyList(), any()))
                .thenReturn(List.of(new DashBoardTableDTO()));
        var result = dashBoardResource.getAllDashBoardTable(ratingDTOS, 2020,companyUserDTO);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }



    @Test
    @DisplayName("Should return a csv file")
    void getTemplateShouldReturnCSVFile() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName("test");
        fileDTO.setContent("test");
        when(dashboardService.getDataSourceTemplate()).thenReturn(fileDTO);
        assertEquals(HttpStatus.OK,dashBoardResource.getTemplate(new CompanyUserDTO()).getStatusCode());
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
        companyUserDTO.setCompanyName("company");
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

        dashBoardResource.saveRanges(companyUserDTO,rangeDTOS);

        verify(dashboardService, times(1)).saveRanges(rangeDTOS, companyUserDTO);
    }

    @Test
    @DisplayName("Should receive exception when save report")
    void saveReportsAndGetException() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("test");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO();
        reportValuesDTO.setReport(reportDTO);
        List<ReportValuesDTO> reportValuesDTOS = List.of(reportValuesDTO);
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();

        doThrow(new RuntimeException())
                .when(this.dashboardService).saveReportForUser( companyUserDTO,reportDTO);
        assertEquals(HttpStatus.BAD_REQUEST,this.dashBoardResource.saveReports(reportDTO,companyUserDTO).getStatusCode());
    }

    @Test
    @DisplayName("Should receive exception when save csv")
    void saveCsvAndGetException() throws IOException {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        byte[] bytes = Files.readAllBytes(Paths.get(VasConstants.CSV_FILEPATH));
        MultipartFile file = new MockMultipartFile(VasConstants.CSV_NAME,bytes);
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        doThrow(new RuntimeException())
                .when(this.dashboardService).saveCsv( file,VasConstants.CSV_NAME,companyUserDTO, year, "Global");
        assertEquals(HttpStatus.NOT_ACCEPTABLE,this.dashBoardResource.uploadFile( VasConstants.CSV_NAME,year,"Global",companyUserDTO,file).getStatusCode());
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

        List<ReportValuesDTO> result = dashBoardResource.getReportsValueByReport(reportDTO,new CompanyUserDTO()).getBody();

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

        List<ReportValuesDTO> result = dashBoardResource.getReportsValueByReport(reportDTO,new CompanyUserDTO()).getBody();

        assertEquals(reportValuesDTOS, result);
    }

    @Test
    @DisplayName("Should return a list of countries when the user is authenticated")
    void getCountrysWhenUserIsAuthenticatedThenReturnListOfCountries() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("company");
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
        companyUserDTO.setCompanyName("company");
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

        var result = dashBoardResource.getReportsValueByReport(reportDTO,new CompanyUserDTO());

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

        var result = dashBoardResource.getReportsValueByReport(reportDTO,new CompanyUserDTO());

        assertEquals(1, result.getBody().size());
    }

    @Test
    @DisplayName("Should receive exception when share report")
    void shareReportsAndGetException() {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportName("test");
        reportDTO.setType(Type.Company);
        reportDTO.setCompany("test");
        reportDTO.setCompanyUserName("test");
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();

        doThrow(new RuntimeException())
                .when(this.dashboardService).shareReportForUser(reportDTO);
        assertEquals(HttpStatus.BAD_REQUEST,this.dashBoardResource.shareReport(reportDTO,companyUserDTO).getStatusCode());
    }

}