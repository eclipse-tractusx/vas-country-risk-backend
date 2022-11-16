package com.catenax.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A CompanyGates.
 */
@Entity
@Table(name = "t_company_gates")
@Setter
@Getter
@ToString
public class CompanyGates implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "gate_name", nullable = false)
    private String gateName;

    @NotNull
    @Column(name = "company_group", nullable = false, unique = true)
    private String companyGroup;

    @ManyToOne
    @JsonIgnoreProperties(value = { "companyUsers", "companyGates" }, allowSetters = true)
    private Company company;


}
