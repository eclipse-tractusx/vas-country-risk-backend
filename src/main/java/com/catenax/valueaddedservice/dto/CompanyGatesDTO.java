package com.catenax.valueaddedservice.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.CompanyGates} entity.
 */
public class CompanyGatesDTO implements Serializable {

    private Long id;

    @NotNull
    private String gateName;

    @NotNull
    private String companyGroup;


}
