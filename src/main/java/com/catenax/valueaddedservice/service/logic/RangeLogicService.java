package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.service.RangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RangeLogicService {

    @Autowired
    RangeService rangeService;

    public void saveRanges(List<RangeDTO> rangeDTOS, CompanyUserDTO companyUserDTO)  {
        List<RangeDTO> list = rangeService.getAllRangesList(companyUserDTO);
        if (list.isEmpty()) {
            rangeDTOS.forEach(rangeDTO -> rangeService.save(rangeDTO));
        } else {
            rangeDTOS.forEach(rangeDTO -> rangeService.updateRanges(rangeDTO));
        }
    }
}
