package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.domain.ReportValues;
import com.catenax.valueaddedservice.dto.ReportDTO;
import com.catenax.valueaddedservice.dto.ReportValuesDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ReportValues} and its DTO {@link ReportValuesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportValuesMapper extends EntityMapper<ReportValuesDTO, ReportValues> {

    ReportValuesDTO toDto(ReportValues s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
