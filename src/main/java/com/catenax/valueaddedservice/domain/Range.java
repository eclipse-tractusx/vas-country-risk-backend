package com.catenax.valueaddedservice.domain;

import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Range.
 */
@Entity
@Table(name = "t_range")
@Setter
@Getter
@ToString
public class Range implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "range_name", nullable = false)
    private RangeType range;

    @NotNull
    @Column(name = "range_value", nullable = false)
    private Integer value;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dataSources", "ranges", "regions" }, allowSetters = true)
    private CompanyUser companyUser;


}
