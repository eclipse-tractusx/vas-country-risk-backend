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
package org.eclipse.tractusx.valueaddedservice.utils;

import org.eclipse.tractusx.valueaddedservice.domain.enumeration.BusinessPartnerRole;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.gate.GateGeoCoordinateDto;
import org.eclipse.tractusx.valueaddedservice.dto.bpdm.pool.*;

import java.util.Collection;
import java.util.Map;

public class BpdmEndpointsMappingUtils {


    public static BusinessPartnerDTO mapToBusinessPartnerDto(PoolLegalEntityDto legalEntityDto, Map<String, Collection<BusinessPartnerRole>> bpnRolesMap) {
        BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();

        businessPartnerDTO.setBpn(legalEntityDto.getBpnl());
        businessPartnerDTO.setLegalName(legalEntityDto.getLegalName());

        PoolAddressDto legalAddress = legalEntityDto.getPoolAddressDto();
        if (legalAddress != null) {
            mapPhysicalOrAlternativeAddress(businessPartnerDTO, legalAddress);
        }

        setBusinessPartnerRoles(businessPartnerDTO, legalEntityDto.getBpnl(), bpnRolesMap);

        return businessPartnerDTO;
    }

    public static BusinessPartnerDTO mapToBusinessPartnerDto(PoolSiteDto poolSiteDto, Map<String, Collection<BusinessPartnerRole>> bpnRolesMap) {
        BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();
        businessPartnerDTO.setBpn(poolSiteDto.getBpns());
        businessPartnerDTO.setLegalName(poolSiteDto.getName());

        PoolAddressDto siteMainAddress = poolSiteDto.getMainAddress();
        if (siteMainAddress != null) {
            mapPhysicalOrAlternativeAddress(businessPartnerDTO, siteMainAddress);
        }

        setBusinessPartnerRoles(businessPartnerDTO, poolSiteDto.getBpns(), bpnRolesMap);

        return businessPartnerDTO;
    }

    public static BusinessPartnerDTO mapToBusinessPartnerDto(PoolAddressDto poolAddressDto, Map<String, Collection<BusinessPartnerRole>> bpnRolesMap) {
        BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();
        businessPartnerDTO.setBpn(poolAddressDto.getBpna());
        businessPartnerDTO.setLegalName(poolAddressDto.getName());

        mapPhysicalOrAlternativeAddress(businessPartnerDTO, poolAddressDto);

        setBusinessPartnerRoles(businessPartnerDTO, poolAddressDto.getBpna(), bpnRolesMap);

        return businessPartnerDTO;
    }


    public static void setBusinessPartnerRoles(BusinessPartnerDTO businessPartnerDTO, String bpn, Map<String, Collection<BusinessPartnerRole>> bpnRolesMap) {
        Collection<BusinessPartnerRole> roles = bpnRolesMap.get(bpn);
        if (roles != null) {
            businessPartnerDTO.setSupplier(roles.contains(BusinessPartnerRole.SUPPLIER));
            businessPartnerDTO.setCustomer(roles.contains(BusinessPartnerRole.CUSTOMER));
        }
    }


    public static void mapPhysicalOrAlternativeAddress(BusinessPartnerDTO businessPartnerDTO, PoolAddressDto addressDto) {
        if (addressDto.getPhysicalPostalAddress() != null) {
            mapAddressDetails(businessPartnerDTO, addressDto.getPhysicalPostalAddress());
        } else if (addressDto.getAlternativePostalAddressDto() != null) {
            PoolExtendedAlternativePostalAddressDto alternativeAddress = addressDto.getAlternativePostalAddressDto();
            businessPartnerDTO.setCity(alternativeAddress.getCity());
            businessPartnerDTO.setCountry(alternativeAddress.getCountry());
            businessPartnerDTO.setZipCode(alternativeAddress.getPostalCode());
            // Assumindo que a alternativa também tem coordenadas geográficas
            if (alternativeAddress.getGeographicCoordinates() != null) {
                GateGeoCoordinateDto coordinates = alternativeAddress.getGeographicCoordinates();
                businessPartnerDTO.setLatitude(String.valueOf(coordinates.getLatitude()));
                businessPartnerDTO.setLongitude(String.valueOf(coordinates.getLongitude()));
            }
        }
    }

    public static void mapAddressDetails(BusinessPartnerDTO businessPartnerDTO, PoolExtendedPhysicalPostalAddressDto physicalAddress) {
        businessPartnerDTO.setStreet(physicalAddress.getStreet().getName());
        businessPartnerDTO.setHouseNumber(physicalAddress.getStreet().getHouseNumber());
        businessPartnerDTO.setCity(physicalAddress.getCity());
        businessPartnerDTO.setCountry(physicalAddress.getPoolCountry().getTechnicalKey());
        businessPartnerDTO.setZipCode(physicalAddress.getPostalCode());
        if (physicalAddress.getGeographicCoordinates() != null) {
            GateGeoCoordinateDto coordinates = physicalAddress.getGeographicCoordinates();
            businessPartnerDTO.setLatitude(String.valueOf(coordinates.getLatitude()));
            businessPartnerDTO.setLongitude(String.valueOf(coordinates.getLongitude()));
        }
    }

}
