package com.catenax.valueaddedservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.CompanyGates} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CompanyGatesDTO implements Serializable {

    private Long id;

    @NotNull
    private String gateName;

    @NotNull
    private String companyGateValue;



}
