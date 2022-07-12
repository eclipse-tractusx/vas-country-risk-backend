package com.catenax.valueaddedservice.service.mapper;

import com.catenax.valueaddedservice.domain.Region;
import com.catenax.valueaddedservice.domain.RegionValue;
import com.catenax.valueaddedservice.dto.RegionDTO;
import com.catenax.valueaddedservice.dto.RegionValueDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link RegionValue} and its DTO {@link RegionValueDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegionValueMapper extends EntityMapper<RegionValueDTO, RegionValue> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    RegionValueDTO toDto(RegionValue s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);
}
