package com.catenax.valueaddedservice.service;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.DashBoardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing {@link DataSource}.
 */
@Service
@Transactional
public class DashboardService {

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DataSourceValueService dataSourceValueService;

    private final Logger log = LoggerFactory.getLogger(DashboardService.class);

    public List<DashBoardDTO> getTableInfo() {
        log.debug("Request to get Table Info");
        List<DashBoardDTO> dataSourceDTOS = dataSourceValueService.findByScoreGreaterThan(Float.valueOf(-1));
        return dataSourceDTOS;
    }

}
