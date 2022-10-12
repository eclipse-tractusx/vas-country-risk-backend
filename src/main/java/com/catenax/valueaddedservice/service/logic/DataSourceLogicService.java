package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class DataSourceLogicService {

    @Autowired
    DataSourceService dataSourceService;

    public List<DataSourceDTO> findRatingsByYearAndCompanyUser(Integer year, CompanyUserDTO companyUserDTO){
        List<DataSourceDTO>  dataSourceDTOS = dataSourceService.findRatingsByYearAndTypeGlobal(year);
        dataSourceDTOS.addAll(dataSourceService.findRatingByYearAndUser(year,companyUserDTO));
        return dataSourceDTOS;
    }

    public List<DataSourceDTO> findRatingsByCompanyUser(CompanyUserDTO companyUserDTO){
       return  dataSourceService.findRatingByUser(companyUserDTO);
    }
}
