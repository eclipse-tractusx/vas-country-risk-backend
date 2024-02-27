/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.RangeType;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.RangeDTO;
import org.eclipse.tractusx.valueaddedservice.service.RangeService;
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
        String sanitizedRange = StringEscapeUtils.escapeJava(rangeDTOS.toString());
        String sanitizedCompany = StringEscapeUtils.escapeJava(companyUserDTO.toString());
        log.debug("saveRanges save new ranges {} for companyUser {}",sanitizedRange,sanitizedCompany);
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
    
    @Cacheable(value = "vas-range", key = "{#root.methodName , {#companyUserDTO.name,#companyUserDTO.email,#companyUserDTO.companyName}}", unless = "#result == null")
    public List<RangeDTO> getUserRangesOrDefault(CompanyUserDTO companyUserDTO) {
        String sanitizedCompany = StringEscapeUtils.escapeJava(companyUserDTO.toString());
        log.debug("getUserRangesOrDefault get ranges for companyUser {}",sanitizedCompany);
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
