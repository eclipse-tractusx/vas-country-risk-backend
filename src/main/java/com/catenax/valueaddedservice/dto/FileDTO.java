package com.catenax.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.File} entity.
 */
@Setter
@Getter
@ToString
public class FileDTO implements Serializable {

    private Long id;

    @Schema(example = "TestFile")
    @NotNull
    private String fileName;

    private String content;

    private String contentContentType;

    @Schema(example = "1.0")
    private Integer version;

    private CompanyUserDTO companyUser;



}
