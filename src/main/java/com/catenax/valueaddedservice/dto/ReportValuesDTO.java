package com.catenax.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Setter
@Getter
@ToString
public class ReportValuesDTO implements Serializable {

    private Long id;

    @Schema(example = "Range")
    private String name;

    private Object objectValue;

    @JsonIgnore
    private ReportDTO report;

}
