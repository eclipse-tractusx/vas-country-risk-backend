package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ShareDTO implements Serializable {

    private Long id;

    @Schema(example = "BPN-NUMBER")
    private String bpn;

    @Schema(example = "Portugal")
    private String country;

    @Schema(example = "PT")
    private String iso2;

    private List<DataSourceDTO> rating;

}
