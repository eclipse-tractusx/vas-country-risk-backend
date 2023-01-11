package com.catenax.valueaddedservice.domain;

import com.catenax.valueaddedservice.utils.DataBaseJsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;

/**
 * A ReportValues.
 */
@Entity
@Table(name = "t_report_value")
@Setter
@Getter
@ToString
public class ReportValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Convert(converter = DataBaseJsonConverter.class)
    @Column(name="object_value", columnDefinition = "jsonb")
    private HashMap<String,Object> objectValue;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reportValues" }, allowSetters = true)
    private Report report;


}
