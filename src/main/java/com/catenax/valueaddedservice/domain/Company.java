package com.catenax.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Company.
 */
@Entity
@Table(name = "t_company")
@Setter
@Getter
@ToString
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "company_group")
    private String companyGroup;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<CompanyGates> companyGates = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties(value = { "company" }, allowSetters = true)
    private Set<CompanyUser> companyUsers = new HashSet<>();


}
