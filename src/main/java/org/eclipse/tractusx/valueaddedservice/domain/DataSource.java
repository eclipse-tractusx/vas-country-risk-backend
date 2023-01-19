/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.springframework.data.annotation.CreatedDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
