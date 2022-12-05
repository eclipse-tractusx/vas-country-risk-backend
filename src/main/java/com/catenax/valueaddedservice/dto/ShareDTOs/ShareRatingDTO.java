package com.catenax.valueaddedservice.dto.ShareDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Region} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShareRatingDTO implements Serializable {

    @Schema(example = "Fake Rating")
    private String dataSourceName = "";

    @Schema(example = "100")
    private Float score = 0F;

}