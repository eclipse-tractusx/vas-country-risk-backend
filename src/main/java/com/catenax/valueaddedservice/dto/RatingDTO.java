package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RatingDTO implements Serializable {

    @Schema(example = "Fake Rating")
    private String dataSourceName = "";

    @Schema(example = "100")
    private Float weight = 0F;

    public RatingDTO(String json) {
        try {
            RatingDTO ratingDTO = new ObjectMapper().readValue(json,RatingDTO.class);
            this.dataSourceName = ratingDTO.getDataSourceName();
            this.weight = ratingDTO.getWeight();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
