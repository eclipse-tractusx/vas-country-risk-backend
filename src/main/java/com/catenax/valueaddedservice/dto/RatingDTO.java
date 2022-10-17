package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Region} entity.
 */
@Setter
@Getter
@ToString
public class RatingDTO implements Serializable {

    @Schema(example = "Fake Rating")
    private String dataSourceName = "";

    @Schema(example = "100")
    private Float weight = 0F;

}
