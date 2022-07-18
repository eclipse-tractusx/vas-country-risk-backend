package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.Type;
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

    @NotNull
    private String dataSourceName;

    @NotNull
    private Type type;

    @NotNull
    private Integer year;

    private String fileName;

    private CompanyUserDTO companyUser;


}
