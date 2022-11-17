package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Company;
import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.dto.CompanyDTO;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link CompanyUser} and its DTO {@link CompanyUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyUserMapper extends EntityMapper<CompanyUserDTO, CompanyUser> {

    CompanyUserDTO toDto(CompanyUser s);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
