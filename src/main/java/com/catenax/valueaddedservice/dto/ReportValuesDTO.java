package com.catenax.valueaddedservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Setter
@Getter
@ToString
public class ReportValuesDTO implements Serializable {

    private Long id;

    private String name;

    private Object objectValue;

    private ReportDTO report;

}
