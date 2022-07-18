package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import com.catenax.valueaddedservice.dto.DataSourceValueDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link DataSourceValue} and its DTO {@link DataSourceValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataSourceValueMapper extends EntityMapper<DataSourceValueDTO, DataSourceValue> {
    @Mapping(target = "dataSource", source = "dataSource", qualifiedByName = "dataSourceId")
    DataSourceValueDTO toDto(DataSourceValue s);

    @Named("dataSourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DataSourceDTO toDtoDataSourceId(DataSource dataSource);
}
