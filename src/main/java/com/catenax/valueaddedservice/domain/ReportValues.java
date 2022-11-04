package com.catenax.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ReportValues.
 */
@Entity
@Table(name = "t_report_value")
@Setter
@Getter
@ToString
@TypeDef(name = "json", typeClass = JsonType.class)
public class ReportValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Type(type = "json")
    @Column(name="object_value", columnDefinition = "jsonb")
    private Object objectValue ;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reportValues" }, allowSetters = true)
    private Report report;


}
