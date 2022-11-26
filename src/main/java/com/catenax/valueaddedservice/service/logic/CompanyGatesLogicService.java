package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyDTO;
import com.catenax.valueaddedservice.dto.CompanyGatesDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.service.CompanyGatesService;
import com.catenax.valueaddedservice.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CompanyGatesLogicService {

    @Autowired
    CompanyService companyUserLogicService;

    @Autowired
    CompanyGatesService companyGatesService;

    public List<CompanyGatesDTO> getGatesForCompanyUser(CompanyUserDTO companyUserDTO){
        Optional<CompanyDTO> companyDTOOptional = companyUserLogicService.getCompanyByCompanyName(companyUserDTO.getCompanyName());
        if(companyDTOOptional.isPresent()){
           List<CompanyGatesDTO> companyGatesDTOS = companyGatesService.findByCompanyGroup(companyDTOOptional.get());
           return companyGatesDTOS;
        }
        return new ArrayList<>();
    }



}
