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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    @Column(name = "companyName", nullable = false)
    private String companyName;

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "dataSourceValues", "companyUser" }, allowSetters = true)
    private Set<DataSource> dataSources = new HashSet<>();

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "companyUser" }, allowSetters = true)
    private Set<Range> ranges = new HashSet<>();

    @OneToMany(mappedBy = "companyUser")
    @JsonIgnoreProperties(value = { "regionValues", "companyUser" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "companyUsers", "companyGroup" }, allowSetters = true)
    private Company company;
}
