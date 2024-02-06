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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.eclipse.tractusx.valueaddedservice.dto.DataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.InputSharingBusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.InputSharingDataSourceDTO;
import org.eclipse.tractusx.valueaddedservice.dto.ShareDTOs.ShareDTO;
import org.eclipse.tractusx.valueaddedservice.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "Sharing Controller")
@SecurityRequirements({@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "open_id_scheme")})
@Slf4j
public class SharingResource {

 
    @Autowired
    DashboardService dashboardService;

    @Operation(summary = "Retrieves ratings based on inserted year and Company User")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ratings of inserted custom year retrieved with success"),
            @ApiResponse(responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/sharing/getAllRatingsForCompany")
    public ResponseEntity<List<DataSourceDTO>> getAllRatingsForCompany(@RequestParam(value = "year", defaultValue = "0", required = false) Integer year,
                                                                       CompanyUserDTO companyUserDTO) {
        List<DataSourceDTO> dataSourceDTOList;
        log.debug( "REST request to get ratings based on inserted year and Company User");
        dataSourceDTOList = dashboardService.findRatingsByYearAndCompanyUserCompany(year, companyUserDTO);
        return ResponseEntity.ok().body(dataSourceDTOList);
    }


    @Operation(summary = "Retrieves Mapped ratings to the Business Partners based on inserted year, Company User, Ratings, BPN")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ratings of inserted custom year retrieved with success"),
            @ApiResponse(responseCode = "401", description = "Authentication Required", content = @Content)})
    @GetMapping("/sharing/getAllRatingsScoresForEachBpn")
    public ResponseEntity<List<ShareDTO>> getAllRatingsScoresForEachBpn(@NotNull @Parameter(name = "datasource[]", description = "", required = true) @Valid @RequestParam(value = "datasource[]", required = true) List<InputSharingDataSourceDTO> datasource,
                                                                        @NotNull @Parameter(name = "bpns[]", description = "", required = true) @Valid @RequestParam(value = "bpns[]", required = true) List<InputSharingBusinessPartnerDTO> businessPartnerDTOS,
                                                                        CompanyUserDTO companyUserDTO) {
        log.debug( "REST request to retrieve Mapped ratings to the Business Partners based on inserted year, Company User, Ratings, BPN");
        List<ShareDTO> shareDTOS;
        shareDTOS = dashboardService.findRatingsScoresForEachBpn(datasource, businessPartnerDTOS, companyUserDTO);
        return ResponseEntity.ok().body(shareDTOS);
    }


}
