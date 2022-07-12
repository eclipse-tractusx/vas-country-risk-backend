package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Range} entity.
 */
@Setter
@Getter
@ToString
public class RangeDTO implements Serializable {

    private Long id;

    @NotNull
    private RangeType range;

    @NotNull
    private Integer value;

    private String description;

    private CompanyUserDTO companyUser;


}
