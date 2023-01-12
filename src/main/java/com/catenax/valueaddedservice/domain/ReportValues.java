package com.catenax.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.io.Serializable;


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


//    @Column(name="object_value", columnDefinition = "jsonb")
//    @Convert(converter = DataBaseJsonConverter.class)
//    @JdbcTypeCode(SqlTypes.JSON)
//    private Object objectValue;

    @Type(JsonType.class)
    @Column(name="object_value", columnDefinition = "jsonb")
    private Object objectValue ;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reportValues" }, allowSetters = true)
    private Report report;


}
