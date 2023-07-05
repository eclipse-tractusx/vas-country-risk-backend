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
package org.eclipse.tractusx.valueaddedservice.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.config.ApplicationVariables;
import org.eclipse.tractusx.valueaddedservice.domain.DataSource;
import org.eclipse.tractusx.valueaddedservice.dto.*;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.ShareDTO;
import org.eclipse.tractusx.valueaddedservice.service.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
@Slf4j
public class DashboardService {

    @Autowired
    UploadAndDownloadLogicService uploadAndDownloadLogicService;

    @Autowired
    WorldMapAndTableLogicService worldMapAndTableLogicService;

    @Autowired
    CompanyUserLogicService companyUserLogicService;

    @Autowired
    RangeLogicService rangeLogicService;

    @Autowired
    DataSourceLogicService dataSourceLogicService;

    @Autowired
    CountryLogicService countryLogicService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    @Autowired
    ReportLogicService reportLogicService;

    @Autowired
    CompanyGatesLogicService companyGatesLogicService;

    @Autowired
    ShareLogicService shareLogicService;

    @Autowired
    ApplicationVariables applicationVariables;

    @Autowired
    BusinessPartnersLogicService businessPartnersLogicService;

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getTableInfo(year, ratingDTOList, companyUser,applicationVariables.getToken(),
                applicationVariables.getAuthPropertiesDTO().getRoles(applicationVariables.getAuthPropertiesDTO().getClientResource()));
    }

    public List<DashBoardWorldMapDTO> getWorldMapInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getWorldMapInfo(year, ratingDTOList, companyUser);
    }

    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year, CompanyUserDTO companyUserDTO) {
        return dataSourceLogicService.findRatingsByYearAndCompanyUser(year, companyUserDTO);
    }

    public FileDTO getDataSourceTemplate() {
        return uploadAndDownloadLogicService.getDataSourceTemplate();
    }

    public void saveCsv(MultipartFile file, String dataSourceName, CompanyUserDTO companyUserDTO, Integer year, String type) throws IOException {
        CompanyUserDTO companyUserDTOUse = companyUserLogicService.getOrCreate(companyUserDTO);
        uploadAndDownloadLogicService.saveCsv(file, dataSourceName, companyUserDTOUse, year, type);
        dataSourceLogicService.invalidateAllCache();
    }

    //Ranges
    public void saveRanges(List<RangeDTO> rangeDTOS, CompanyUserDTO companyUserDTO) {
        CompanyUserDTO companyUserDTO1 = companyUserLogicService.getOrCreate(companyUserDTO);
        rangeLogicService.saveRanges(rangeDTOS, companyUserDTO1);
        rangeLogicService.invalidateAllCache();
    }

    public List<Integer> getYearsOfUserRatings(CompanyUserDTO companyUserDTO) {
        List<Integer> list = new ArrayList<>();
        list.addAll(dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO).stream().map(DataSourceDTO::getYearPublished).collect(Collectors.toSet()));
        return list;
    }

    public List<RangeDTO> getUserRangesOrDefault(CompanyUserDTO companyUser) {
        return rangeLogicService.getUserRangesOrDefault(companyUser);
    }

    public List<CountryDTO> getCountryFilterByISO2(CompanyUserDTO companyUserDTO) {
        return countryLogicService.getCountryFilterByISO2(companyUserDTO,
                applicationVariables.getToken(),
                applicationVariables.getAuthPropertiesDTO().getRoles(applicationVariables.getAuthPropertiesDTO().getClientResource()));
    }

    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUserDTO) {
        return businessPartnersLogicService.getExternalBusinessPartners(companyUserDTO,applicationVariables.getToken(),
                applicationVariables.getAuthPropertiesDTO().getRoles(applicationVariables.getAuthPropertiesDTO().getClientResource()));

    }

    public List<CountryDTO> getCountryByAssociatedBPtoUser(CompanyUserDTO companyUserDTO) {
        return countryLogicService.getAssociatedCountries(companyUserDTO,
                applicationVariables.getToken(),
                applicationVariables.getAuthPropertiesDTO().getRoles(applicationVariables.getAuthPropertiesDTO().getClientResource()));
    }

    public List<ReportDTO> getReportsByCompanyUser(CompanyUserDTO companyUserDTO) {
        List<ReportDTO> reportDTOList = new ArrayList<>();
        List<ReportDTO> reportDTOS = reportLogicService.getGlobalReports();
        List<ReportDTO> companyReports = reportLogicService.getCompanyReports(companyUserDTO);
        List<ReportDTO> reportsForCompanyUser = reportLogicService.getReportsForCompanyUser(companyUserDTO);
        reportDTOList.addAll(companyReports);
        reportDTOList.addAll(reportsForCompanyUser);
        reportDTOList.addAll(reportDTOS);
        return reportDTOList;
    }

    public void saveReportForUser(CompanyUserDTO companyUserDTO, ReportDTO reportDTO) {
        CompanyUserDTO companyUserDTO1 = companyUserLogicService.getOrCreate(companyUserDTO);
        reportLogicService.saveReport(reportDTO, companyUserDTO1);
        reportLogicService.invalidateAllCache();
    }

    public void shareReportForUser(ReportDTO reportDTO) {
        CompanyUserDTO companyUserDTOUse = companyUserLogicService.findByNameEmailAndCompany(reportDTO.getCompanyUserName()
        , reportDTO.getEmail(), reportDTO.getCompany());

        reportLogicService.saveReport(reportDTO, companyUserDTOUse);
        reportLogicService.invalidateAllCache();
    }

    public List<ReportValuesDTO> getReportValues(ReportDTO reportDTO) {
        return reportLogicService.getReportValues(reportDTO);
    }
    public List<CompanyGatesDTO> getGatesForCompanyUser(CompanyUserDTO companyUserDTO) {
        return companyGatesLogicService.getGatesForCompanyUser(companyUserDTO);
    }

    public List<DataSourceDTO> findRatingsByYearAndCompanyUserCompany(Integer year, CompanyUserDTO companyUserDTO){
        List<DataSourceDTO> dataSourceDTOList = dataSourceLogicService.findRatingsByYearAndCompanyUserCompany(year,companyUserDTO);
        return dataSourceDTOList;
    }

    public List<ShareDTO> findRatingsScoresForEachBpn(List<DataSourceDTO> datasource, List<BusinessPartnerDTO> businessPartner, CompanyUserDTO companyUser) {
        return shareLogicService.findRatingsScoresForEachBpn(datasource, businessPartner ,companyUser,applicationVariables.getToken(),
                applicationVariables.getAuthPropertiesDTO().getRoles(applicationVariables.getAuthPropertiesDTO().getClientResource()));
    }

    public void deleteReportFromUserById(Long reportId,CompanyUserDTO companyUserDTO)  {
        reportLogicService.deleteReportById(reportId,companyUserDTO);
        reportLogicService.invalidateAllCache();

    }

    public void deleteRatingFromUserById(Long ratingId,CompanyUserDTO companyUserDTO)  {
        dataSourceLogicService.deleteDataSourceById(ratingId,companyUserDTO);
        dataSourceLogicService.invalidateAllCache();
    }

    public List<CompanyUserDTO> getUsersFromCompany(CompanyUserDTO companyUserDTO) {
        return companyUserLogicService.getUsersFromCompany(companyUserDTO);
    }



}
