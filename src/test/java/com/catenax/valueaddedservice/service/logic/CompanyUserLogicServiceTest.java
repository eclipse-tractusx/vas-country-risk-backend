package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.dto.AuthPropertiesDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.CompanyUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyUserLogicServiceTest {

    @Mock
    CompanyUserService companyUserService;

    @Mock
    ApplicationVariables applicationVariables;

    @InjectMocks
    CompanyUserLogicService companyUserLogicService;

    @Test
    @DisplayName("Should return true when the company name, email and name are the same")
    void validateUserAndTokenAreTheSameWhenCompanyNameEmailAndNameAreTheSameThenReturnTrue() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("TestCompany");
        companyUserDTO.setEmail("test@email.com");
        companyUserDTO.setName("TestName");

        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("TestCompany");
        authPropertiesDTO.setEmail("test@email.com");
        authPropertiesDTO.setName("TestName");

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);

        assertTrue(companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO));
    }

    @Test
    @DisplayName("Should return false when the company name, email and name are not the same")
    void validateUserAndTokenAreTheSameWhenCompanyNameEmailAndNameAreNotTheSameThenReturnFalse() {
        CompanyUserDTO companyUserDTO = new CompanyUserDTO();
        companyUserDTO.setCompanyName("companyName");
        companyUserDTO.setEmail("email");
        companyUserDTO.setName("name");

        AuthPropertiesDTO authPropertiesDTO = new AuthPropertiesDTO();
        authPropertiesDTO.setCompanyName("companyName1");
        authPropertiesDTO.setEmail("email1");
        authPropertiesDTO.setName("name1");

        when(applicationVariables.getAuthPropertiesDTO()).thenReturn(authPropertiesDTO);

        boolean result = companyUserLogicService.validateUserAndTokenAreTheSame(companyUserDTO);

        assertFalse(result);
    }
}