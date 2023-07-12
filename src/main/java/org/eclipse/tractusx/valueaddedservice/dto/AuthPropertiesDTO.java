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
import org.eclipse.tractusx.valueaddedservice.constants.VasConstants;
import org.eclipse.tractusx.valueaddedservice.domain.CompanyUser;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A DTO for the {@link CompanyUser} entity.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthPropertiesDTO implements Serializable {


    @Schema(example = "John")
    @NotNull
    @JsonProperty("name")
    private String name = "";

    @Schema(example = "John@email.com")
    @NotNull
    @JsonProperty("email")
    private String email = "";

    @Schema(example = "TestCompany")
    @NotNull
    @JsonProperty("organisation")
    private String companyName = "";

    @Schema(example = "\"CX-CRISK\":{\"roles\":[\"GetUser\"]}")
    @NotNull
    @JsonProperty("resource_access")
    private Object resourceAccess = new LinkedHashMap<>();

    @Schema(example = "12XRisk")
    @NotNull
    @JsonProperty("azp")
    private String clientResource = "";

    @Schema(example = "true")
    @NotNull
    @JsonProperty("isAdmin")
    private Boolean isAdmin = false;

    public List<String> getRoles(String clientId){
        LinkedHashMap list =  (LinkedHashMap) resourceAccess;
        LinkedHashMap clientResources = list.get(clientId) != null ? (LinkedHashMap) list.get(clientId):new LinkedHashMap();
        List<String> rolesList = clientResources.get("roles")  != null ? (List<String>) clientResources.get("roles"):new ArrayList<>();
        this.isAdmin = rolesList.contains(VasConstants.CSV_ROLE_COMPANY_ADMIN);
        return rolesList;
    }

}
