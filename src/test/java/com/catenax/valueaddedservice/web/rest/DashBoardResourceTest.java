package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
import com.catenax.valueaddedservice.service.CountryService;
import com.catenax.valueaddedservice.service.DashboardService;
import com.catenax.valueaddedservice.service.DataSourceService;
import com.catenax.valueaddedservice.service.RangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardResource")
class DashBoardResourceTest {

    @Mock
    DashboardService dashboardService;

    @Mock
    DataSourceService dataSourceService;

    @Mock
    CountryService countryService;

    @Mock
    RangeService rangeService;

    @InjectMocks
    DashBoardResource dashboardResource;

    CompanyUserDTO companyUserDTO;

    @BeforeEach
    void setUp() {
        companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("test user");
        companyUserDTO.setCompany("test");
        companyUserDTO.setEmail("test_user@mail.com");
        companyUserDTO.setId(1L);
    }

    @Test
    @DisplayName("Should return a list of dashboardtabledto when the year is not null")
    void getAllDashBoardTableWhenYearIsNotNull() throws IOException {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        when(dashboardService.getTableInfo(anyInt(), anyList(), any(CompanyUserDTO.class)))
                .thenReturn(dashBoardTableDTOS);
        ResponseEntity<List<DashBoardTableDTO>> responseEntity =
                dashboardResource.getAllDashBoardTable(new HashMap<>(), 1, companyUserDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dashBoardTableDTOS, responseEntity.getBody());
    }



    @Test
    @DisplayName("Should return all dashboards when the year is 0")
    void getAllDashBoardTableWhenYearIs0() throws IOException {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        when(dashboardService.getTableInfo(anyInt(), anyList(), any()))
                .thenReturn(dashBoardTableDTOS);
        Map<String, Object> ratings = new HashMap<>();
        ratings.put("ratings", "[]");
        ResponseEntity<List<DashBoardTableDTO>> responseEntity =
                dashboardResource.getAllDashBoardTable(new HashMap<>(), 0, companyUserDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dashBoardTableDTOS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Should return all dashboards when the year is not 0")
    void getAllDashBoardTableWhenYearIsNot0() throws IOException {
        List<DashBoardTableDTO> dashBoardTableDTOS = new ArrayList<>();
        Map<String, Object> ratings = new HashMap<>();
        ratings.put(
                "ratings",
                "[%7B%22id%22:17,%22dataSourceName%22:%22Basel+Score+2020+(Rechnung+Maximal+(Maximum+Score+%2B1)+-+(Basel+Score+*+10))%22,%22type%22:%22Global%22,%22yearPublished%22:2020,%22fileName%22:null,%22companyUser%22:null,%22weight%22:100%7D]");
        when(dashboardService.getTableInfo(anyInt(), anyList(), any(CompanyUserDTO.class)))
                .thenReturn(dashBoardTableDTOS);
        ResponseEntity<List<DashBoardTableDTO>> responseEntity =
                dashboardResource.getAllDashBoardTable(new HashMap<>(), 1, companyUserDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(dashBoardTableDTOS, responseEntity.getBody());
    }
}