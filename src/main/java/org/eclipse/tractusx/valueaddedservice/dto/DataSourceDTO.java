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
package org.eclipse.tractusx.valueaddedservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.valueaddedservice.domain.DataSource;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * A DTO for the {@link DataSource} entity.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties("companyUser")
public class DataSourceDTO implements Serializable {

    private Long id;

    @Schema(example = "Fake Rating")
    @NotNull
    private String dataSourceName;

    @Schema(example = "Custom")
    @NotNull
    private Type type;

    @Schema(example = "2021")
    @NotNull
    private Integer yearPublished;

    @Schema(example = "Test Company Rating")
    private String fileName;

    private CompanyUserDTO companyUser;


    public DataSourceDTO(String json) {

        try {
            DataSourceDTO dataSourceDTO = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(json, DataSourceDTO.class);
            this.dataSourceName = dataSourceDTO.getDataSourceName();
            this.yearPublished = dataSourceDTO.getYearPublished();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
