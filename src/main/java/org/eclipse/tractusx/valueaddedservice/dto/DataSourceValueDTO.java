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
package org.eclipse.tractusx.valueaddedservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.valueaddedservice.domain.DataSourceValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link DataSourceValue} entity.
 */
@Setter
@Getter
@ToString
public class DataSourceValueDTO implements Serializable {

    private Long id;

    @Schema(example = "Germany", required = true)
    @NotNull
    private String country;

    @Schema(example = "DEU", required = true)
    @Size(max = 2)
    private String iso3;

    @Schema(example = "DE", required = true)
    @Size(max = 3)
    private String iso2;

    @Schema(example = "Europe", required = true)
    private String continent;

    @Schema(example = "90", required = true)
    private Float score;

    private DataSourceDTO dataSource;


}
