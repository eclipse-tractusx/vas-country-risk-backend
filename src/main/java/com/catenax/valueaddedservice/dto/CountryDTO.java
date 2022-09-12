package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Country} entity.
 */
@Setter
@Getter
@ToString
public class CountryDTO implements Serializable {

    private Long id;

    @Schema(example = "Germany", required = true)
    @NotNull
    private String country;

    @Schema(example = "DEU", required = true)
    @Size(max = 2)
    private String iso3;

    @Schema(example = "DE", required = true)
    @Size(max = 3)
    private String iso2;

    @Schema(example = "Europe", required = true)
    private String continent;


}
