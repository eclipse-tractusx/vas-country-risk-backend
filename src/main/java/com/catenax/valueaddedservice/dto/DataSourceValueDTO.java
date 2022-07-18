package com.catenax.valueaddedservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.DataSourceValue} entity.
 */
@Setter
@Getter
@ToString
public class DataSourceValueDTO implements Serializable {

    private Long id;

    @NotNull
    private String country;

    @Size(max = 2)
    private String iso3;

    @Size(max = 3)
    private String iso2;

    private String continent;

    private Float score;

    private DataSourceDTO dataSource;


}
