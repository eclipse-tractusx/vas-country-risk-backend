package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardWorldMapDTO implements Serializable {

    private CountryDTO country;

    @Schema(example = "90")
    private Float score = 0F;



}
