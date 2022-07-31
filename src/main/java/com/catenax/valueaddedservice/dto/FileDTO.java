package com.catenax.valueaddedservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Lob;
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

    @Lob
    private byte[] content;

    private String contentContentType;

    private Integer version;

    private CompanyUserDTO companyUser;



}
