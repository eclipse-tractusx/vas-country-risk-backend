package com.catenax.valueaddedservice.dto;

import com.catenax.valueaddedservice.domain.CompanyGroup;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link CompanyGroup} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CompanyGroupDTO implements Serializable {

    private Long id;

    private String companyGroup;

    private List<CompanyGatesDTO> companyGates = new ArrayList<>();

}
