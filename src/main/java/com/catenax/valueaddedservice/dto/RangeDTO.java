package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Range} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("id")
public class RangeDTO implements Serializable {


    private Long id;

    @Schema(example = "Max")
    @NotNull
    private RangeType range;

    @Schema(example = "80")
    @NotNull
    private Integer value;

    @Schema(example = "Max value")
    private String description;

    private CompanyUserDTO companyUser;

}
