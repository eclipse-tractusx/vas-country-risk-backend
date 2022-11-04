package com.catenax.valueaddedservice.domain;

import com.catenax.valueaddedservice.domain.enumeration.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Report.
 */
@Entity
@Table(name = "t_report")
@Setter
@Getter
@ToString
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "company_user_name")
    private String companyUserName;

    @Column(name = "company")
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @OneToMany(mappedBy = "report")
    @JsonIgnoreProperties(value = { "report" }, allowSetters = true)
    private Set<ReportValues> reportValues = new HashSet<>();


}
