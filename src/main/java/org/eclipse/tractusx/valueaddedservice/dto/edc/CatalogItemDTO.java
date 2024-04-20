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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents a catalog item available for negotiation")
public class CatalogItemDTO {

    @Schema(description = "Unique identifier of the catalog item", example = "5191c813-97c7-4a50-8acc-5ad500772640", required = true)
    private String id;

    @Schema(description = "Identifier of the offer associated with the catalog item", example = "offer123", required = true)
    private String offerId;

    @Schema(description = "Provider of the catalog item", example = "Provider A")
    private String provider;

    @Schema(description = "Subject of the catalog item", example = "cx-taxo:ReadAccessPoolForCatenaXMember")
    private String subject;

    @Schema(description = "Description of the catalog item", example = "Grants the Catena-X Member read access to the Pool API...")
    private String description;
}

