package com.catenax.valueaddedservice.service.logic;

import com.catenax.valueaddedservice.constants.VasConstants;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.RangeDTO;
import com.catenax.valueaddedservice.service.RangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        rangeDTOMin.setValue(VasConstants.MIN_DEFAULT_USER_RANGE);
        ranges.add(rangeDTOMin);
        RangeDTO rangeDTOBetWeen = new RangeDTO();
        rangeDTOBetWeen.setRange(RangeType.Between);
        rangeDTOBetWeen.setCompanyUser(companyUserDTO);
        rangeDTOBetWeen.setDescription("BetWeen Range");
        rangeDTOBetWeen.setValue(VasConstants.BETWEEN_DEFAULT_USER_RANGE);
        ranges.add(rangeDTOBetWeen);
        RangeDTO rangeDTOMax = new RangeDTO();
        rangeDTOMax.setRange(RangeType.Max);
        rangeDTOMax.setCompanyUser(companyUserDTO);
        rangeDTOMax.setValue(VasConstants.MAX_DEFAULT_USER_RANGE);
        ranges.add(rangeDTOMax);
        return ranges;
    }

    @CacheEvict(value = "vas-range", allEntries = true)
    public void invalidateAllCache() {
        log.debug("invalidateAllCache|vas-range -  invalidated cache - allEntries");
    }
}
