package com.catenax.valueaddedservice.dto;

import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardWorldMapDTO implements Serializable {

    private CountryDTO country;

    private Float score = 0F;



}
