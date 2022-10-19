package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Region} entity.
 */


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListRatingDTO implements Serializable {



    @Schema(example = "Fake Rating")
    private List<RatingDTO> ratingDTOS = new ArrayList<>();

    public ListRatingDTO(String params) throws JsonProcessingException {
        this.ratingDTOS = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(params, new TypeReference<List<RatingDTO>>() {
        });

    }
}
