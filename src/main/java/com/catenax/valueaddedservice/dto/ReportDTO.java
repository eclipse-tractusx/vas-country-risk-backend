package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
public class ReportDTO implements Serializable {

    private Long id;

    private String reportName;

    private String companyUserName;

    private String company;

    private Type type;

    private List<ReportValuesDTO> reportValuesDTOList;

}
