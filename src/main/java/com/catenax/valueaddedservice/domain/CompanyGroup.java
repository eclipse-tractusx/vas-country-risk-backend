package com.catenax.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Company.
 */
@Entity
@Table(name = "t_company_group")
@Setter
@Getter
@ToString
public class CompanyGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company_group",nullable = false, unique = true)
    private String companyGroup;

    @OneToMany(mappedBy = "companyGroup")
    @JsonIgnoreProperties(value = { "companyUsers", "companyGroup" }, allowSetters = true)
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "companyGroup")
    @JsonIgnoreProperties(value = { "companyGroup" }, allowSetters = true)
    private Set<CompanyGates> companyGates = new HashSet<>();


}
