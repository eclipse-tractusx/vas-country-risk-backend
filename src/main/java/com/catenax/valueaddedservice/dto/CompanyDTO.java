package com.catenax.valueaddedservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Company} entity.
 */
@Setter
@Getter
@ToString
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String companyName;

    private String companyGroup;

    private List<CompanyUserDTO> companyUsers;

    private List<CompanyGatesDTO> companyGates;

}
