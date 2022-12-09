package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardTableDTO implements Serializable {

    private Long id;

    @Schema(example = "BPN-NUMBER")
    private String bpn;

    @Schema(example = "Divape Company")
    private String legalName;

    @Schema(example = "1st")
    private String street;

    @Schema(example = "Sutteridge")
    private String houseNumber;

    @Schema(example = "633104")
    private String zipCode;

    @Schema(example = "Covilhã")
    private String city;

    @Schema(example = "Portugal")
    private String country;

    @Schema(example = "90")
    private Float score = 0F;

    @Schema(example = "Fake Rating")
    private String rating = "";

    @Schema(example = "107.6185727")
    private String longitude;

    @Schema(example = "-6.6889038")
    private String latitude;

}
