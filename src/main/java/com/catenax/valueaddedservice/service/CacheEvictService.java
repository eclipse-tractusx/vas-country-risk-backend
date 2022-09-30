package com.catenax.valueaddedservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static javax.management.timer.Timer.ONE_HOUR;

@Service
@Transactional
@Slf4j
public class CacheEvictService {

    @Autowired
    CountryService countryService;


    @Scheduled(fixedRate = ONE_HOUR)
    @CacheEvict(value = { "vas" })
    public void clearCache() {
        log.info("Cache '{}' cleared.");
    }
}
