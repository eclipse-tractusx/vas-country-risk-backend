package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.CompanyUser} entity.
 */
@Setter
@Getter
@ToString
public class CompanyUserDTO implements Serializable {

    private Long id;

    @Schema(example = "John", required = true)
    @NotNull
    private String name;

    @Schema(example = "John@email.com", required = true)
    @NotNull
    private String email;

    @Schema(example = "TestCompany", required = true)
    @NotNull
    private String company;


}
