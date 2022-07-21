package com.catenax.valueaddedservice.dto;

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

    private String dataSourceName = "";

    private Float weight = 0F;

}
