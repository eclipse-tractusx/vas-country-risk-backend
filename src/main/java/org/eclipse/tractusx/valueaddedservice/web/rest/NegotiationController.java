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
package org.eclipse.tractusx.valueaddedservice.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.edc.CatalogItemDTO;
import org.eclipse.tractusx.valueaddedservice.dto.edc.NegotiationRequestDTO;
import org.eclipse.tractusx.valueaddedservice.dto.edc.NegotiationResponseDTO;
import org.eclipse.tractusx.valueaddedservice.service.logic.EdcLogicService;
import org.eclipse.tractusx.valueaddedservice.service.logic.NegotiationServiceLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negotiation")
@Tag(name = "Negotiation Controller")
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "open_id_scheme")
@Slf4j
public class NegotiationController {

    @Autowired
    private EdcLogicService edcLogicService;

    @Autowired
    private NegotiationServiceLogic negotiationService;

    @Operation(summary = "Retrieves catalog items available for negotiation",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved catalog items",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CatalogItemDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Authentication Required"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping("/queryCatalog")
    public ResponseEntity<List<CatalogItemDTO>> queryCatalog() {
        List<CatalogItemDTO> catalogItems = edcLogicService.queryCatalog();
        return ResponseEntity.ok(catalogItems);
    }

    @Operation(summary = "Triggers negotiation with selected items",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Negotiation initiated successfully",
                            content = @Content(mediaType = "text/plain")),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Authentication Required"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping("/triggerNegotiation")
    public ResponseEntity<List<NegotiationResponseDTO>> triggerNegotiation(@RequestBody List<NegotiationRequestDTO> negotiationItems) {
        return ResponseEntity.ok().body(negotiationService.triggerNegotiation(negotiationItems));
    }
}