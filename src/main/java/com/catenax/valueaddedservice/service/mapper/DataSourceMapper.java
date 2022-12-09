package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import com.catenax.valueaddedservice.dto.DataSourceDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link DataSource} and its DTO {@link DataSourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface DataSourceMapper extends EntityMapper<DataSourceDTO, DataSource> {

    DataSourceDTO toDto(DataSource s);

    @Named("companyUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyUserDTO toDtoCompanyUserId(CompanyUser companyUser);

}
