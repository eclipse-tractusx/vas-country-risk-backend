package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.dto.CompanyUserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link CompanyUser} and its DTO {@link CompanyUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyUserMapper extends EntityMapper<CompanyUserDTO, CompanyUser> {}
