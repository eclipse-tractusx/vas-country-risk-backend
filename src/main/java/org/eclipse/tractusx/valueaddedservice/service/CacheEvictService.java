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
package org.eclipse.tractusx.valueaddedservice.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.service.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_MINUTE;

@Service
@Slf4j
public class CacheEvictService {

    @Autowired
    CountryLogicService countryLogicService;

    @Autowired
    ExternalBusinessPartnersLogicService externalBusinessPartnersLogicService;

    @Autowired
    DataSourceLogicService dataSourceLogicService;

    @Autowired
    RangeLogicService rangeLogicService;

    @Autowired
    ReportLogicService reportLogicService;

    @Scheduled(fixedRate = ONE_HOUR)
    public void clearCacheCountry() {
        countryLogicService.invalidateAllCache();
        log.info("Cache for country's cleared.");
    }

    @Scheduled(fixedRate = ONE_MINUTE*5)
    public void clearCacheBpn() {
        externalBusinessPartnersLogicService.invalidateAllCache();
        log.info("Cache for bpn cleared.");
    }

    @Scheduled(fixedRate = ONE_MINUTE*15)
    public void clearCacheDataSource() {
        dataSourceLogicService.invalidateAllCache();
        log.info("Cache for DataSource cleared.");
    }

    @Scheduled(fixedRate = ONE_HOUR)
    public void clearCacheRange() {
        rangeLogicService.invalidateAllCache();
        log.info("Cache for user range cleared.");
    }

    @Scheduled(fixedRate = ONE_HOUR)
    public void clearCacheReports() {
        reportLogicService.invalidateAllCache();
        log.info("Cache for Reports cleared.");
    }
}
