package com.catenax.valueaddedservice.dto;

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

    @NotNull
    private String fileName;

    private String content;

    private String contentContentType;

    private Integer version;

    private CompanyUserDTO companyUser;



}
