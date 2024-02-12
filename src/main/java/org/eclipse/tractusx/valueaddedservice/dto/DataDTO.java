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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@JsonIgnoreProperties({ "weight" })
public class DataDTO implements Serializable {

    @Schema(example = "Germany")
    private String country;

    @Schema(example = "70")
    private Float score = 0F;
    @Schema(example = "CPI Rating 2021")
    private String dataSourceName = "";

    @Schema(example = "100.00")
    private Float weight;

    @Schema(example = "DEU")
    @Size(max = 2)
    private String iso3;

    @Schema(example = "DE")
    @Size(max = 3)
    private String iso2;

    @Schema(example = "Europe")
    private String continent;

    public DataDTO(String country, Float score, String dataSourceName,String iso3,String iso2 ,String continent) {
        this.country = country;
        this.score = score;
        this.dataSourceName = dataSourceName;
        this.iso3 = iso3;
        this.iso2 = iso2;
        this.continent = continent;
    }
}
