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
package org.eclipse.tractusx.valueaddedservice.dto.edc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object with negotiation status")
public class NegotiationResponseDTO {

    @Schema(description = "Unique identifier of the catalog item", example = "1", required = true)
    private String id;

    @Schema(description = "Identifier of the offer associated with the catalog item", example = "offer123", required = true)
    private String offerId;

    @Schema(description = "Provider of the catalog item", example = "Provider A")
    private String provider;

    @Schema(description = "Status of negotiation of the catalog item", example = "Negotiated")
    private String status;

    @JsonIgnore
    @Schema(description = "Auth Code for requesting the endpoint", example = "utasdbvhsarpoausighasd")
    private String authCode;

    @JsonIgnore
    @Schema(description = "Endpoint for the Final Request", example = "http://localhost:80/finalRequest")
    private String endpoint;
}
