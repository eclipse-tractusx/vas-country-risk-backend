package com.catenax.valueaddedservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link com.catenax.valueaddedservice.domain.Company} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CompanyDTO implements Serializable {

    private Long id;

    @NotNull
    private String companyName;

    private CompanyGroupDTO companyGroup;

    private List<CompanyUserDTO> companyUserDTOS = new ArrayList<>();

}
