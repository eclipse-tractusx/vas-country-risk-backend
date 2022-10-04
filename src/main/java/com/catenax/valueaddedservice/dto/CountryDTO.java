package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Country} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

    @Schema(example = "-2.9814344", required = true)
    private String latitude;

    @Schema(example = "23.8222636", required = true)
    private String longitude;

    @Schema(example = "11", required = true)
    private Long totalBpn;

    public CountryDTO(String country, String iso3, String iso2, String continent) {
        this.country = country;
        this.iso3 = iso3;
        this.iso2 = iso2;
        this.continent = continent;
    }
}
