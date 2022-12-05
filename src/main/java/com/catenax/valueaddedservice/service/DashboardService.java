package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.dto.ShareDTOs.ShareDTO;
import com.catenax.valueaddedservice.service.logic.*;
import lombok.extern.slf4j.Slf4j;
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

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getTableInfo(year, ratingDTOList, companyUser);
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
        return countryLogicService.getCountryFilterByISO2(companyUserDTO);
    }

    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUserDTO) {
        return externalBusinessPartnersLogicService.getExternalBusinessPartners(companyUserDTO);

    }

    public List<CountryDTO> getCountryByAssociatedBPtoUser(CompanyUserDTO companyUserDTO) {
        return countryLogicService.getAssociatedCountries(companyUserDTO);
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
        return shareLogicService.findRatingsScoresForEachBpn(datasource, businessPartner ,companyUser);
    }

}
