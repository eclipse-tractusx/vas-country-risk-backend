package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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
@JsonIgnoreProperties("id")
public class CompanyUserDTO implements Serializable {

    private Long id;

    @Schema(example = "John")
    @JsonProperty("name")
    @NotNull
    @NotEmpty
    private String name ;

    @Schema(example = "John@email.com")
    @Email
    @NotNull
    @JsonProperty("email")
    private String email ;

    @Schema(example = "TestCompany")
    @NotNull
    @JsonProperty("companyName")
    private String companyName ;



}
