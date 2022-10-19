package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReportValuesDTO implements Serializable {

    private Long id;

    @Schema(example = "Range")
    private String name;

    private Object objectValue;

    @JsonIgnore
    private ReportDTO report;

}
