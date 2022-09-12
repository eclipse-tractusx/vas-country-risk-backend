package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.CompanyUser} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompanyUserDTO implements Serializable {

    private Long id;

    @Schema(example = "John", required = true)
    @NotNull
    @JsonProperty("name")
    private String name;

    @Schema(example = "John@email.com", required = true)
    @NotNull
    @JsonProperty("email")
    private String email;

    @Schema(example = "TestCompany", required = true)
    @NotNull
    @JsonProperty("company")
    private String company;


}
