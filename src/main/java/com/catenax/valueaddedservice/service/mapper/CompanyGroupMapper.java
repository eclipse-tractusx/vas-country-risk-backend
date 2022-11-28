package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.CompanyGroup;
import com.catenax.valueaddedservice.dto.CompanyGroupDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link CompanyGroup} and its DTO {@link CompanyGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyGroupMapper extends EntityMapper<CompanyGroupDTO, CompanyGroup> {}
