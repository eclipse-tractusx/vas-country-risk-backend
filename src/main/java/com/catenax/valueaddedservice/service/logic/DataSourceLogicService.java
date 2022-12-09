package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.DataSourceValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DataSourceLogicService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    @Autowired
    CompanyUserLogicService companyUserLogicService;

    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#year,#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year, CompanyUserDTO companyUserDTO){
        log.debug("findRatingsByYearAndCompanyUser {}",companyUserDTO);
        List<DataSourceDTO> dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        List<DataSourceDTO> companyRatings = dataSourceService.findByYearPublishedAndCompanyUserCompanyNameAndType(year, companyUserDTO, Type.Company);
        List<DataSourceDTO> dataSourceDTOByYearAndUser = dataSourceService.findRatingByYearAndUser(year,companyUserDTO);
        dataSourceDTOS.addAll(dataSourceDTOByYearAndUser);
        dataSourceDTOS.addAll(companyRatings);
        return dataSourceDTOS;
    }

    
    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByCompanyUser(CompanyUserDTO companyUserDTO){
        log.debug("findRatingsByCompanyUser {}",companyUserDTO);
        return dataSourceService.findRatingByUser(companyUserDTO);

    }

    @CacheEvict(value = "vas-datasource", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-Datasource -  invalidated cache - allEntries");
    }

    @Cacheable(value = "vas-datasource", key = "{#root.methodName , {#year,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<DataSourceDTO> findRatingsByYearAndCompanyUserCompany(Integer year, CompanyUserDTO companyUserDTO){
        log.debug("findRatingsByYearAndCompanyUser {}",companyUserDTO);
        List<DataSourceDTO>  dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        List<DataSourceDTO> companyRatings = dataSourceService.findByYearPublishedAndCompanyUserCompanyNameAndType(year, companyUserDTO, Type.Company);
        dataSourceDTOS.addAll(companyRatings);
        return dataSourceDTOS;
    }

    public void deleteDataSourceById(Long ratingId,CompanyUserDTO companyUserDTO){
        Optional<DataSourceDTO> optionalDataSourceDTO = dataSourceService.findOne(ratingId);
        DataSourceDTO dataSourceDTO = optionalDataSourceDTO.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
        if( (dataSourceDTO.getCompanyUser().getCompanyName().equalsIgnoreCase(companyUserDTO.getCompanyName())
                && dataSourceDTO.getCompanyUser().getName().equalsIgnoreCase(companyUserDTO.getName())
                    && dataSourceDTO.getCompanyUser().getEmail().equalsIgnoreCase(companyUserDTO.getEmail()))
                || companyUserLogicService.isAdmin()){
            List<DataSourceValueDTO> dataSourceValueDTOS = dataSourceValueService.findByDataSource(dataSourceDTO);
            dataSourceValueDTOS.forEach(dataSourceValueDTO -> dataSourceValueService.delete(dataSourceValueDTO.getId()));
            dataSourceService.delete(dataSourceDTO.getId());
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
