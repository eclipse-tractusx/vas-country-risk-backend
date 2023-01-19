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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ReportDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ReportValuesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.ReportRepository;
import org.eclipse.tractusx.valueaddedservice.service.ReportService;
import org.eclipse.tractusx.valueaddedservice.service.ReportValuesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
        ReportDTO reportDTO = new ReportDTO(null, "Fake Rating", "John", "Test Company","John@email.com", Type.Custom,new ArrayList<>());
        Map<String,Object> map = new HashMap<>();
        map.put("teste","teste");
        reportDTO.setReportValuesDTOList(
                Arrays.asList(new ReportValuesDTO(null, "Range", map, null)));
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
        companyUserDTO.setCompanyName("TestCompany");

        when(reportService.findByCompanyUserNameAndCompanyAndType(
                companyUserDTO.getName(), companyUserDTO.getCompanyName(), Type.Custom))
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
        companyUserDTO.setCompanyName("TestCompany");

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
                companyUserDTO.getName(), companyUserDTO.getCompanyName(), Type.Custom))
                .thenReturn(reports);

        List<ReportDTO> result = reportLogicService.getReportsForCompanyUser(companyUserDTO);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return the reports for the company")
    void getCompanyReportsShouldReturnTheReportsForTheCompany() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("Test Company");
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setCompany(companyUserDTO.getCompanyName());
        List<ReportDTO> reportDTOList = Arrays.asList(reportDTO);
        when(reportService.findByCompanyAndType(companyUserDTO.getCompanyName(), Type.Company))
                .thenReturn(reportDTOList);

        List<ReportDTO> result = reportLogicService.getCompanyReports(companyUserDTO);

        assertEquals(reportDTOList, result);
    }


}