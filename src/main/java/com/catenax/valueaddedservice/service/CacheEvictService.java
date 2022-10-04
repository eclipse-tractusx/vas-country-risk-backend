package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.service.logic.CountryLogicService;
import com.catenax.valueaddedservice.service.logic.DataSourceLogicService;
import com.catenax.valueaddedservice.service.logic.ExternalBusinessPartnersLogicService;
import com.catenax.valueaddedservice.service.logic.RangeLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_MINUTE;

@Service
@Transactional
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

    @Scheduled(fixedRate = ONE_HOUR)
    public void clearCacheCountry() {
        countryLogicService.invalidateAllCache();
        log.info("Cache for country's cleared.");
    }

    @Scheduled(fixedRate = ONE_MINUTE*15)
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
}
