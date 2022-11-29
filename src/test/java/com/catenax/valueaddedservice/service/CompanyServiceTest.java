package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.dto.CompanyDTO;
import com.catenax.valueaddedservice.repository.CompanyRepository;
import com.catenax.valueaddedservice.service.mapper.CompanyMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyService companyService;



    @Test
    @DisplayName("Should save")
    void save() {
        CompanyDTO companyDTO = new CompanyDTO();

        CompanyDTO result = companyService.save(companyDTO);

        assertNotEquals(companyDTO,result);
    }


}