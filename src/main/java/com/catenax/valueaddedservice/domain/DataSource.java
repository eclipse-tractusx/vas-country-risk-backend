package com.catenax.valueaddedservice.domain;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DataSource.
 */
@Entity
@Table(name = "t_data_source")
@Setter
@Getter
@ToString
public class DataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_source_name", nullable = false)
    private String dataSourceName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "data_source_type", nullable = false)
    private Type type;

    @NotNull
    @Column(name = "year_published", nullable = false)
    private Integer yearPublished;

    @Column(name = "file_name")
    private String fileName;


    @CreatedDate
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @OneToMany(mappedBy = "dataSource")
    @JsonIgnoreProperties(value = { "dataSource" }, allowSetters = true)
    private Set<DataSourceValue> dataSourceValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "dataSources", "ranges", "regions" }, allowSetters = true)
    private CompanyUser companyUser;


}
