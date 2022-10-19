package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO implements Serializable {

    private Long id;

    @Schema(example = "Fake Rating")
    private String reportName;

    @Schema(example = "John")
    private String companyUserName;

    @Schema(example = "Test Company")
    private String company;

    @Schema(example = "Company")
    private Type type;

    private List<ReportValuesDTO> reportValuesDTOList;

}
