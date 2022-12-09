package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.CompanyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CompanyUserLogicService {

    @Autowired
    CompanyUserService companyUserService;

    @Autowired
    ApplicationVariables applicationVariables;

    public CompanyUserDTO getOrCreate(CompanyUserDTO companyUserDTO)  {
        CompanyUserDTO companyUserDTOUse = companyUserService.findBYNameEmailAndCompany(companyUserDTO);
        if(companyUserDTOUse == null){
            companyUserDTOUse =companyUserService.save(companyUserDTO);
        }

        return companyUserDTOUse;
    }

    public boolean validateUserAndTokenAreTheSame(CompanyUserDTO companyUserDTO){
        return companyUserDTO.getCompanyName().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getCompanyName()) &&
                companyUserDTO.getEmail().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getEmail()) &&
                companyUserDTO.getName().equalsIgnoreCase(applicationVariables.getAuthPropertiesDTO().getName());
    }

    public boolean isAdmin(){
        return applicationVariables.getAuthPropertiesDTO().getIsAdmin();
    }







}
