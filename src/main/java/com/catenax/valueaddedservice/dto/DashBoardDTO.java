package com.catenax.valueaddedservice.dto;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardDTO implements Serializable {

    private Long id;

    private String bpn;

    private String legalName;

    private String address;

    private String city;

    private String country;

    private Float score;

    private String rating;

    public DashBoardDTO(Long id,String country, Float score, String rating) {
        this.id = id;
        this.country = country;
        this.score = score;
        this.rating = rating;
    }
}
