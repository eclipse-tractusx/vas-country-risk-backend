package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.dto.CompanyGatesDTO;
import com.catenax.valueaddedservice.repository.CompanyGatesRepository;
import com.catenax.valueaddedservice.service.mapper.CompanyGatesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class CompanyGatesServiceTest {

    @Mock
    private CompanyGatesRepository companyGatesRepository;

    @Mock
    private CompanyGatesMapper companyGatesMapper;

    @InjectMocks
    private CompanyGatesService companyGatesService;



    @Test
    @DisplayName("Should save")
    void save() {
        CompanyGatesDTO companyGatesDTO = new CompanyGatesDTO();

        CompanyGatesDTO result = companyGatesService.save(companyGatesDTO);

        assertNotEquals(companyGatesDTO,result);
    }


}