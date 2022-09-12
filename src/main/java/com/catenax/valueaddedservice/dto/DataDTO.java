package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties({ "weight" })
public class DataDTO implements Serializable {

    @Schema(example = "Germany")
    private String country;

    @Schema(example = "70")
    private Float score = 0F;

    private String dataSourceName = "";

    private Float weight;

    public DataDTO(String country, Float score, String dataSourceName) {
        this.country = country;
        this.score = score;
        this.dataSourceName = dataSourceName;
    }
}
