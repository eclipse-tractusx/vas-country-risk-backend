package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Company;
import com.catenax.valueaddedservice.domain.CompanyGroup;
import com.catenax.valueaddedservice.dto.CompanyDTO;
import com.catenax.valueaddedservice.dto.CompanyGroupDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "companyGroup", source = "companyGroup", qualifiedByName = "companyGroupId")
    CompanyDTO toDto(Company s);

    @Named("companyGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyGroupDTO toDtoCompanyGroupId(CompanyGroup companyGroup);
}
