package com.catenax.valueaddedservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DashBoardWorldMapDTO implements Serializable {

    private String country;

    private Float score = 0F;

    private List<BusinessPartnerDTO> businessPartnerDTOList;

}
