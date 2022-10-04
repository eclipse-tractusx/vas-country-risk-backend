package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.service.RangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        log.debug("saveRanges save new ranges {} for companyUser {}",rangeDTOS,companyUserDTO);
        List<RangeDTO> list = rangeService.getUserRanges(companyUserDTO);
        if (list.isEmpty()) {
            rangeDTOS.forEach(rangeDTO -> {
                rangeDTO.setCompanyUser(companyUserDTO);
                rangeService.save(rangeDTO);
            });
        } else {
            rangeDTOS.forEach(rangeDTO -> {
                rangeDTO.setCompanyUser(companyUserDTO);
                rangeService.updateRanges(rangeDTO);
            });
        }
    }
    @Transactional
    @Cacheable(value = "vas-range", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.company}}", unless = "#result == null")
    public List<RangeDTO> getUserRangesOrDefault(CompanyUserDTO companyUserDTO) {
        log.debug("getUserRangesOrDefault get ranges for companyUser {}",companyUserDTO);
        List<RangeDTO> ranges = rangeService.getUserRanges(companyUserDTO);
        if (!ranges.isEmpty()) {
            return ranges;
        }
        RangeDTO rangeDTOMin = new RangeDTO();
        rangeDTOMin.setRange(RangeType.Min);
        rangeDTOMin.setCompanyUser(companyUserDTO);
        rangeDTOMin.setDescription("Min Range");
        rangeDTOMin.setValue(25);
        ranges.add(rangeDTOMin);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUserDTO);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(50);
        ranges.add(rangeDTOBetWeen);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUserDTO);
        rangeDTOMax.setDescription("Max Range");
        rangeDTOMax.setValue(100);
        ranges.add(rangeDTOMax);
        return ranges;
    }

    @CacheEvict(value = "vas-range", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-range -  invalidated cache - allEntries");
    }
}
