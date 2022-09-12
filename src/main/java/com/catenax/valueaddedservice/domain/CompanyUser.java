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
 * A CompanyUser.
 */
@Entity
@Table(name = "t_company_user")
@Setter
@Getter
@ToString
public class CompanyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "company", nullable = false)
    private String company;

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "dataSourceValues", "companyUser" }, allowSetters = true)
    private Set<DataSource> dataSources = new HashSet<>();

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "companyUser" }, allowSetters = true)
    private Set<Range> ranges = new HashSet<>();

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "regionValues", "companyUser" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();


}
