package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Country;
import com.catenax.valueaddedservice.dto.CountryDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
