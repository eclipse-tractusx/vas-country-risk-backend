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
package org.eclipse.tractusx.valueaddedservice.dto.bpdm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.deserializers.PhysicalPostalAddressDtoDeserializer;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateGeoCoordinateDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateStreetDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolAdministrativeAreaLevel;

@Getter
@Setter
@ToString

@JsonDeserialize(using = PhysicalPostalAddressDtoDeserializer.class)
public class PhysicalPostalAddressDto {
    private GateGeoCoordinateDto geographicCoordinates;
    private String country;

    private PoolAdministrativeAreaLevel administrativeAreaLevel1;
    private String simpleAdministrativeAreaLevel1;

    private PoolAdministrativeAreaLevel administrativeAreaLevel2;
    private String simpleAdministrativeAreaLevel2;

    private PoolAdministrativeAreaLevel administrativeAreaLevel3;
    private String simpleAdministrativeAreaLevel3;

    private String postalCode;
    private String city;
    private String district;
    private GateStreetDto street;
    private String companyPostalCode;
    private String industrialZone;
    private String building;
    private String floor;
    private String door;
}
