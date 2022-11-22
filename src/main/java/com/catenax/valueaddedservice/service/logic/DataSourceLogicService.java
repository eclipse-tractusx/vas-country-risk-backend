package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.BusinessPartnerDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataSourceLogicService {

    @Autowired
    DataSourceService dataSourceService;

    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#year,#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year, CompanyUserDTO companyUserDTO){
        log.debug("findRatingsByYearAndCompanyUser {}",companyUserDTO);
        List<DataSourceDTO>  dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        dataSourceDTOS.addAll(dataSourceService.findRatingByYearAndUser(year,companyUserDTO));
        return dataSourceDTOS;
    }

    
    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByCompanyUser(CompanyUserDTO companyUserDTO){
        log.debug("findRatingsByCompanyUser {}",companyUserDTO);
        return  dataSourceService.findRatingByUser(companyUserDTO);

    }

    @CacheEvict(value = "vas-datasource", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Datasource -  invalidated cache - allEntries");
    }

    public List<DataSourceDTO> findRatingsByCompanyCustom(Integer year, CompanyUserDTO companyUserDTO){
        log.debug("findRatingsCompanyShare");
        return dataSourceService.findByYearPublishedAndCompanyUserCompanyAndType(year, companyUserDTO, Type.Company);
    }

    public List<DataSourceDTO> findRatingsByYearAndType(Integer year){
        log.debug("findRatingsCompanyShare");
        return dataSourceService.findRatingsByYearAndTypeGlobal(year);
    }
}
