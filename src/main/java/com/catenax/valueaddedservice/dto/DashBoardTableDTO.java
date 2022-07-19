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
public class DashBoardTableDTO implements Serializable {

    private Long id;

    private String bpn;

    private String legalName;

    private String address;

    private String city;

    private String country;

    private Float score = 0F;

    private String rating = "";

    private Float weight;

    public DashBoardTableDTO(Long id, String country, Float score, String rating) {
        this.id = id;
        this.country = country;
        this.score = score;
        this.rating = rating;
    }
}
