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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO implements Serializable {

    private Long id;

    @NotEmpty
    @Schema(example = "Fake Report")
    private String reportName;

    @Schema(example = "John")
    private String companyUserName;

    @Schema(example = "Test Company")
    private String company;

    @Schema(example = "John@email.com")
    @Email
    private String email ;

    @Schema(example = "Company")
    private Type type;

    @JsonProperty(value = "reportValues")
    private List<ReportValuesDTO> reportValuesDTOList;

}
