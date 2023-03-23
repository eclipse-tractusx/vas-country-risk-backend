package org.eclipse.tractusx.valueaddedservice.dto.bpdm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LegalEntity {

    private String bpn;
    private List<ValueTypeResponse> names;
    private Address legalAddress;
}


