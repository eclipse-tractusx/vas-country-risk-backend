package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.dto.ReportDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {}
