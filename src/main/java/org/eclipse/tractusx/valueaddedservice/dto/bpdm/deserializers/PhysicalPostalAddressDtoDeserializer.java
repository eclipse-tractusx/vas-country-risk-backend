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
package org.eclipse.tractusx.valueaddedservice.dto.bpdm.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.PhysicalPostalAddressDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateGeoCoordinateDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateStreetDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.PoolAdministrativeAreaLevel;

import java.io.IOException;

public class PhysicalPostalAddressDtoDeserializer extends JsonDeserializer<PhysicalPostalAddressDto> {

    @Override
    public PhysicalPostalAddressDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode rootNode = mapper.readTree(jp);

        PhysicalPostalAddressDto address = new PhysicalPostalAddressDto();

        JsonNode adminAreaNode = rootNode.get("administrativeAreaLevel1");
        if (adminAreaNode != null) {
            if (adminAreaNode.isObject()) {
                // Deserialize as object
                PoolAdministrativeAreaLevel adminAreaLevel = mapper.treeToValue(adminAreaNode, PoolAdministrativeAreaLevel.class);
                address.setAdministrativeAreaLevel1(adminAreaLevel);
            } else if (adminAreaNode.isTextual()) {
                // Deserialize as string
                String adminAreaLevelString = adminAreaNode.asText();
                address.setSimpleAdministrativeAreaLevel1(adminAreaLevelString);
            }
        }
        adminAreaNode = rootNode.get("administrativeAreaLevel2");
        if (adminAreaNode != null) {
            if (adminAreaNode.isObject()) {
                // Deserialize as object
                PoolAdministrativeAreaLevel adminAreaLevel = mapper.treeToValue(adminAreaNode, PoolAdministrativeAreaLevel.class);
                address.setAdministrativeAreaLevel2(adminAreaLevel);
            } else if (adminAreaNode.isTextual()) {
                // Deserialize as string
                String adminAreaLevelString = adminAreaNode.asText();
                address.setSimpleAdministrativeAreaLevel2(adminAreaLevelString);
            }
        }
        adminAreaNode = rootNode.get("administrativeAreaLevel3");
        if (adminAreaNode != null) {
            if (adminAreaNode.isObject()) {
                // Deserialize as object
                PoolAdministrativeAreaLevel adminAreaLevel = mapper.treeToValue(adminAreaNode, PoolAdministrativeAreaLevel.class);
                address.setAdministrativeAreaLevel3(adminAreaLevel);
            } else if (adminAreaNode.isTextual()) {
                // Deserialize as string
                String adminAreaLevelString = adminAreaNode.asText();
                address.setSimpleAdministrativeAreaLevel3(adminAreaLevelString);
            }
        }
        address.setGeographicCoordinates(mapper.treeToValue(rootNode.get("geographicCoordinates"), GateGeoCoordinateDto.class));
        address.setCountry(rootNode.path("country").path("name").asText());
        address.setPostalCode(rootNode.path("postalCode").asText(null));
        address.setCity(rootNode.path("city").asText(null));
        address.setDistrict(rootNode.path("district").asText(null));
        address.setStreet(mapper.treeToValue(rootNode.get("street"), GateStreetDto.class));
        address.setCompanyPostalCode(rootNode.path("companyPostalCode").asText(null));
        address.setIndustrialZone(rootNode.path("industrialZone").asText(null));
        address.setBuilding(rootNode.path("building").asText(null));
        address.setFloor(rootNode.path("floor").asText(null));
        address.setDoor(rootNode.path("door").asText(null));

        return address;
    }
}

