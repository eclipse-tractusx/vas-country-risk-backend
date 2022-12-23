package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.web.rest.DashBoardResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;

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

    private static final Logger log = ESAPI.getLogger(DashBoardResource.class);
    @Schema(example = "Fake Rating")
    private String dataSourceName = "";

    @Schema(example = "100")
    private Float weight = 0F;

    public RatingDTO(String json) {
        RatingDTO ratingDTO = new RatingDTO();
        try {
            ratingDTO = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(json, new TypeReference<>() {});
            this.dataSourceName = ratingDTO.getDataSourceName();
            this.weight = ratingDTO.getWeight();
        } catch (JsonProcessingException e) {
            log.error(Logger.EVENT_FAILURE, "Error converting " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
