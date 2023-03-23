package org.eclipse.tractusx.valueaddedservice.dto.bpdm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LegalEntityMatchResponse {
    private Float score;
    private LegalEntity legalEntity;
}
