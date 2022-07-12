package com.catenax.valueaddedservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * A DTO for the {@link react.domain.DataSource} entity.
 */
@Setter
@Getter
@ToString
public class DashBoardDTO implements Serializable {

    private Long id;

    private String bpn;

    private String legalName;

    private String address;

    private String city;

    private String country;

    private Float score;

    private String rating;


}
