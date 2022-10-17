package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.*;
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


    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getTableInfo(year,ratingDTOList,companyUser);
    }

    public List<DashBoardWorldMapDTO> getWorldMapInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getWorldMapInfo(year,ratingDTOList,companyUser);
    }

    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year,CompanyUserDTO companyUserDTO){
       return dataSourceLogicService.findRatingsByYearAndCompanyUser(year,companyUserDTO);
    }

    public FileDTO getDataSourceTemplate(){
        return uploadAndDownloadLogicService.getDataSourceTemplate();
    }

    public void saveCsv(MultipartFile file, String dataSourceName,CompanyUserDTO companyUserDTO) throws IOException {
        CompanyUserDTO companyUserDTOUse = companyUserLogicService.getOrCreate(companyUserDTO);
        uploadAndDownloadLogicService.saveCsv(file,dataSourceName,companyUserDTOUse);
        dataSourceLogicService.invalidateAllCache();
    }

    //Ranges
    public void saveRanges(List<RangeDTO> rangeDTOS,CompanyUserDTO companyUserDTO)  {
        CompanyUserDTO companyUserDTO1 = companyUserLogicService.getOrCreate(companyUserDTO);
        rangeLogicService.saveRanges(rangeDTOS,companyUserDTO1);
        rangeLogicService.invalidateAllCache();
    }

    public List<Integer> getYearsOfUserRatings(CompanyUserDTO companyUserDTO){
        List<Integer> list = new ArrayList<>();
        list.addAll(dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO).stream().map(DataSourceDTO::getYearPublished).collect(Collectors.toSet()));
        return list;
    }

    public List<RangeDTO> getUserRangesOrDefault(CompanyUserDTO companyUser) {
       return rangeLogicService.getUserRangesOrDefault(companyUser);
    }

    public List<CountryDTO> getCountryFilterByISO2(CompanyUserDTO companyUserDTO){
        return countryLogicService.getCountryFilterByISO2(companyUserDTO);
    }

    public List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUserDTO){
        return externalBusinessPartnersLogicService.getExternalBusinessPartners(companyUserDTO);

    }

    public List<CountryDTO> getCountryByAssociatedBPtoUser(CompanyUserDTO companyUserDTO){
        return countryLogicService.getAssociatedCountries(companyUserDTO);
    }


}
