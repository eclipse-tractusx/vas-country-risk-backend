package com.catenax.valueaddedservice.dto;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardTableDTO implements Serializable {

    private Long id;

    private String bpn;

    private String legalName;

    private String address;

    private String city;

    private String country;

    private Float score = 0F;

    private String rating = "";

    private String longitude;

    private String latitude;

}
