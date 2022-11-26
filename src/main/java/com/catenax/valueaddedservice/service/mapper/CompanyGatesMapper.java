package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.CompanyGates;
import com.catenax.valueaddedservice.domain.CompanyGroup;
import com.catenax.valueaddedservice.dto.CompanyGatesDTO;
import com.catenax.valueaddedservice.dto.CompanyGroupDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link CompanyGates} and its DTO {@link CompanyGatesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyGatesMapper extends EntityMapper<CompanyGatesDTO, CompanyGates> {

    CompanyGatesDTO toDto(CompanyGates s);

    @Named("companyGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyGroupDTO toDtoCompanyGroupId(CompanyGroup companyGroup);
}
