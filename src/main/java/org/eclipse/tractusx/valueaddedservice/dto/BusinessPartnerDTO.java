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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BusinessPartnerDTO implements Serializable {
    private Long id;

    @Schema(example = "BPN-NUMBER")
    private String bpn;

    @Schema(example = "Divape Company")
    private String legalName;

    @Schema(example = "1st")
    private String street;

    @Schema(example = "Sutteridge")
    private String houseNumber;

    @Schema(example = "633104")
    private String zipCode;

    @Schema(example = "Covilhã")
    private String city;

    @Schema(example = "Portugal")
    private String country;

    @Schema(example = "107.6185727")
    private String longitude;

    @Schema(example = "-6.6889038")
    private String latitude;

    @Schema(example = "false")
    private Boolean supplier = false;

    @Schema(example = "true")
    private Boolean customer = false;

    public BusinessPartnerDTO(String json) {

        try {
            BusinessPartnerDTO businessPartnerDTO = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(json, BusinessPartnerDTO.class);
            this.bpn = businessPartnerDTO.getBpn();
            this.country = businessPartnerDTO.getCountry();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
