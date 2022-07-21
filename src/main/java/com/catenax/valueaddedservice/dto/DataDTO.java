package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties({ "weight" })
public class DataDTO implements Serializable {

    private String country;

    private Float score = 0F;

    private String dataSourceName = "";

    private Float weight;

    public DataDTO(String country, Float score, String dataSourceName) {
        this.country = country;
        this.score = score;
        this.dataSourceName = dataSourceName;
    }
}
