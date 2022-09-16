package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.*;
import com.catenax.valueaddedservice.service.logic.RangeLogicService;
import com.catenax.valueaddedservice.service.logic.UploadAndDownloadLogicService;
import com.catenax.valueaddedservice.service.logic.WorldMapAndTableLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@CrossOrigin
@Service
@Transactional
@Slf4j
public class DashboardService {

    @Autowired
    UploadAndDownloadLogicService uploadAndDownloadLogicService;

    @Autowired
    WorldMapAndTableLogicService worldMapAndTableLogicService;

    @Autowired
    RangeLogicService rangeLogicService;

    @Value(value = "classpath:config/liquibase/fake-data/dashboard.json")
    private Resource json;

    public List<DashBoardTableDTO> getTableInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {
        return worldMapAndTableLogicService.getTableInfo(year,ratingDTOList,companyUser);
    }

    public List<DashBoardWorldMapDTO> getWorldMapInfo(Integer year, List<RatingDTO> ratingDTOList, CompanyUserDTO companyUser) {

        return worldMapAndTableLogicService.getWorldMapInfo(year,ratingDTOList,companyUser);
    }

    public FileDTO getDataSourceTemplate(){
        return uploadAndDownloadLogicService.getDataSourceTemplate();
    }

    public void saveCsv(MultipartFile file, String dataSourceName,CompanyUserDTO companyUserDTO) throws IOException {
        uploadAndDownloadLogicService.saveCsv(file,dataSourceName,companyUserDTO);
    }

    //Ranges
    public void saveRanges(List<RangeDTO> rangeDTOS,CompanyUserDTO companyUserDTO)  {
        rangeLogicService.saveRanges(rangeDTOS,companyUserDTO);
    }



}
