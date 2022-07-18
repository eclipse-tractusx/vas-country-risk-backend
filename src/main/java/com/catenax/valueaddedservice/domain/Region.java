package com.catenax.valueaddedservice.domain;

import com.catenax.valueaddedservice.domain.enumeration.Type;
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
 * A Region.
 */
@Entity
@Table(name = "t_region")
@Setter
@Getter
@ToString
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "region")
    @JsonIgnoreProperties(value = { "region" }, allowSetters = true)
    private Set<RegionValue> regionValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "dataSources", "ranges", "regions" }, allowSetters = true)
    private CompanyUser companyUser;


}
