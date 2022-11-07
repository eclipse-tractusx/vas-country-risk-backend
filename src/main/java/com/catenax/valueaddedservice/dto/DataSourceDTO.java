package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.DataSource} entity.
 */
@Setter
@Getter
@ToString
public class DataSourceDTO implements Serializable {

    private Long id;

    @Schema(example = "Fake Rating")
    @NotNull
    private String dataSourceName;

    @Schema(example = "Custom")
    @NotNull
    private Type type;

    @Schema(example = "2021")
    @NotNull
    private Integer yearPublished;

    @Schema(example = "Test Company Rating")
    private String fileName;

    private CompanyUserDTO companyUser;


}