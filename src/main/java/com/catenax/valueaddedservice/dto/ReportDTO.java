package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ReportDTO implements Serializable {

    private Long id;

    @Schema(example = "Fake Report")
    private String reportName;

    @Schema(example = "John")
    private String companyUserName;

    @Schema(example = "Test Company")
    private String company;

    @Schema(example = "John@email.com")
    @Email
    private String email ;

    @Schema(example = "Company")
    private Type type;

    @JsonProperty(value = "reportValues")
    private List<ReportValuesDTO> reportValuesDTOList;

}
