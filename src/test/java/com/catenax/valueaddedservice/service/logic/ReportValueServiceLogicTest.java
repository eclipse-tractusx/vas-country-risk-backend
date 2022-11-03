package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import com.catenax.valueaddedservice.repository.ReportRepository;
import com.catenax.valueaddedservice.service.ReportService;
import com.catenax.valueaddedservice.service.ReportValuesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportValueLogicService")
public class ReportValueServiceLogicTest {

    @Mock
    ReportService reportService;

    @Mock
    ReportValuesService reportValuesService;

    @InjectMocks
    ReportLogicService reportLogicService;

    CompanyUserDTO companyUserDTO = new CompanyUserDTO(1L, "John", "john@gmail.com", "TestCompany");

    @Test
    @DisplayName("Update a Value Report")
    void updateValueReport() {
        ReportDTO reportDTO = new ReportDTO(1L, "Fake Rating", "John", "Test Company", Type.Custom,new ArrayList<>());
        ReportValuesDTO reportValuesDTO = new ReportValuesDTO(1L, "Range", "Test Range", null);
        reportDTO.setReportValuesDTOList(Arrays.asList(reportValuesDTO));
    }
}
