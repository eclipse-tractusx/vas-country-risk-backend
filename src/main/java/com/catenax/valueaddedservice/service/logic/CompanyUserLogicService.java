package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.config.ApplicationVariables;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.CompanyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Slf4j
public class CompanyUserLogicService {

    @Autowired
    CompanyUserService companyUserService;

    @Autowired
    ApplicationVariables applicationVariables;

    public CompanyUserDTO getOrCreate(CompanyUserDTO companyUserDTO)  {
        CompanyUserDTO companyUserDTOUse = companyUserService.findByNameEmailAndCompany(companyUserDTO.getName(), companyUserDTO.getEmail(), companyUserDTO.getCompanyName());
        if(companyUserDTOUse == null){
            companyUserDTOUse =companyUserService.save(companyUserDTO);
        }

        return companyUserDTOUse;
    }

    public CompanyUserDTO findByNameEmailAndCompany(String companyUserName, String email, String companyName)  {
        CompanyUserDTO companyUserDTOUse = companyUserService.findByNameEmailAndCompany(companyUserName,email,companyName);
        if(companyUserDTOUse == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return companyUserDTOUse;
    }

    public List<CompanyUserDTO> getUsersFromCompany(CompanyUserDTO companyUserDTO){
        return companyUserService.findAllUserFromCompany(companyUserDTO.getCompanyName());
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
