package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.service.DataSourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataSourceLogicService")
class DataSourceLogicServiceTest {

    @Mock
    DataSourceService dataSourceService;

    @InjectMocks
    DataSourceLogicService dataSourceLogicService;

    @Test
    @DisplayName("Should return an empty list when the companyuserdto is null")
    void findRatingsByCompanyUserWhenCompanyUserDTOIsNull() {
        CompanyUserDTO companyUserDTO = null;
        List<DataSourceDTO> dataSourceDTOS =
                dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO);
        assertTrue(dataSourceDTOS.isEmpty());
    }

    @Test
    @DisplayName("Should return the list of datasourcedto when the companyuserdto is not null")
    void findRatingsByCompanyUserWhenCompanyUserDTOIsNotNull() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        List<DataSourceDTO> dataSourceDTOS = new ArrayList<>();
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("Fake Rating");
        dataSourceDTO.setType(Type.Global);
        dataSourceDTO.setYearPublished(2021);
        dataSourceDTO.setFileName("Test Company Rating");
        dataSourceDTOS.add(dataSourceDTO);
        when(dataSourceService.findRatingByUser(companyUserDTO)).thenReturn(dataSourceDTOS);
        List<DataSourceDTO> result =
                dataSourceLogicService.findRatingsByCompanyUser(companyUserDTO);
        assertNotNull(result);
        assertEquals(1, result.size());
    }



    @Test
    @DisplayName(
            "Should return a list of datasourcedto when the year and companyuserdto are not null")
    void
    findRatingsByYearAndCompanyUserWhenYearAndCompanyUserDTONotNullThenReturnListOfDataSourceDTO() {
        Integer year = 2020;
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompanyName("TestCompany");
        List<DataSourceDTO> dataSourceDTOS = new ArrayList<>();
        DataSourceDTO dataSourceDTO = new DataSourceDTO();
        dataSourceDTO.setId(1L);
        dataSourceDTO.setDataSourceName("Fake Rating");
        dataSourceDTO.setType(Type.Global);
        dataSourceDTO.setYearPublished(2020);
        dataSourceDTO.setFileName("Test Company Rating");
        dataSourceDTOS.add(dataSourceDTO);

        when(dataSourceService.findRatingsByYearAndTypeGlobal(year)).thenReturn(dataSourceDTOS);
        when(dataSourceService.findRatingByYearAndUser(year, companyUserDTO))
                .thenReturn(dataSourceDTOS);

        List<DataSourceDTO> result =
                dataSourceLogicService.findRatingsByYearAndCompanyUser(year, companyUserDTO);

        assertNotNull(result);
        assertEquals(2, result.size());
    }


}