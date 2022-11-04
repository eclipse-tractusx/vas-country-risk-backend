package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.CompanyUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyUserLogicServiceTest {

    @Mock
    CompanyUserService companyUserService;

    @InjectMocks
    CompanyUserLogicService companyUserLogicService;

    @Test
    @DisplayName("Should return the companyuser when the companyuser is already exist")
    void getOrCreateWhenCompanyUserIsAlreadyExistThenReturnTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setId(1L);
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");

        when(companyUserService.findBYNameEmailAndCompany(companyUserDTO))
                .thenReturn(companyUserDTO);

        CompanyUserDTO companyUserDTOResult = companyUserLogicService.getOrCreate(companyUserDTO);

        assertEquals(companyUserDTOResult, companyUserDTO);
    }

    @Test
    @DisplayName("Should save and return the companyuser when the companyuser is not exist")
    void getOrCreateWhenCompanyUserIsNotExistThenSaveAndReturnTheCompanyUser() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setName("John");
        companyUserDTO.setEmail("John@email.com");
        companyUserDTO.setCompany("TestCompany");

        when(companyUserService.findBYNameEmailAndCompany(companyUserDTO)).thenReturn(null);
        when(companyUserService.save(companyUserDTO)).thenReturn(companyUserDTO);

        CompanyUserDTO result = companyUserLogicService.getOrCreate(companyUserDTO);

        assertEquals(companyUserDTO, result);
    }
}