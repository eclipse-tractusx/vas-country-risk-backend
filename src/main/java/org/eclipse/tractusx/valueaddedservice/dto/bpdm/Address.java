package org.eclipse.tractusx.valueaddedservice.dto.bpdm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Address {
    private String country;
    private List<ValueTypeDto> postCodes;
    private List<ValueTypeDto> localities;
    private List<ValueTypeDto> thoroughfares;
    private GeoCoordinates geographicCoordinates;
}
