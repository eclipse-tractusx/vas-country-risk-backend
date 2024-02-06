/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DataSourceValue.
 */
@Entity
@Table(name = "t_data_source_value")
@Setter
@Getter
@ToString
public class DataSourceValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Size(max = 3)
    @Column(name = "iso_3", length = 3)
    private String iso3;

    @Size(max = 2)
    @Column(name = "iso_2", length = 2)
    private String iso2;

    @Column(name = "continent")
    private String continent;

    @Column(name = "score")
    private Float score;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dataSourceValues", "companyUser" }, allowSetters = true)
    private DataSource dataSource;


}
