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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.valueaddedservice.dto.BusinessPartnerDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.eclipse.tractusx.valueaddedservice.constants.VasConstants.CSV_ROLE_READ_CUSTOMER;
import static org.eclipse.tractusx.valueaddedservice.constants.VasConstants.CSV_ROLE_READ_SUPPLIER;

@Service
@Slf4j
public class BusinessPartnersLogicService {

    @Autowired
    RequestLogicService requestLogicService;


    public synchronized List<BusinessPartnerDTO> getExternalBusinessPartners(CompanyUserDTO companyUserDTO, String token, List<String> roles) {
        log.debug("Getting BusinessPartners");

        return filterBusinessPartnersByRole(requestLogicService.handleRequestsToBpdm(roles), roles);
    }




    private List<BusinessPartnerDTO> filterBusinessPartnersByRole(List<BusinessPartnerDTO> businessPartnerDTOS, List<String> roles) {
        List<BusinessPartnerDTO> filteredBusinessPartnerDTOS;
        log.debug("Roles {}",roles);
        log.debug("Code Roles {} {}",CSV_ROLE_READ_SUPPLIER,CSV_ROLE_READ_CUSTOMER);

        if (roles.stream().anyMatch(role -> role.equalsIgnoreCase(CSV_ROLE_READ_SUPPLIER)) &&
                roles.stream().anyMatch(role -> role.equalsIgnoreCase(CSV_ROLE_READ_CUSTOMER))) {
            // User has both roles, no need to filter
            filteredBusinessPartnerDTOS = businessPartnerDTOS;
        } else if (roles.stream().anyMatch(role -> role.equalsIgnoreCase(CSV_ROLE_READ_SUPPLIER))) {
            // User can only read suppliers and those who are not customers
            filteredBusinessPartnerDTOS = filterSuppliers(businessPartnerDTOS);
        } else if (roles.stream().anyMatch(role -> role.equalsIgnoreCase(CSV_ROLE_READ_CUSTOMER))) {
            // User can only read customers and those who are not suppliers
            filteredBusinessPartnerDTOS = filterCustomers(businessPartnerDTOS);
        } else {
            // User has neither role, can only see those who are neither suppliers nor customers
            filteredBusinessPartnerDTOS = filterNeither(businessPartnerDTOS);
        }
        return filteredBusinessPartnerDTOS;
    }

    private List<BusinessPartnerDTO> filterSuppliers(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> bpn.getSupplier() || !bpn.getCustomer())
                .peek(bpn -> bpn.setCustomer(false))
                .toList();
    }

    private List<BusinessPartnerDTO> filterCustomers(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> bpn.getCustomer() || !bpn.getSupplier())
                .peek(bpn -> bpn.setSupplier(false))
                .toList();
    }

    private List<BusinessPartnerDTO> filterNeither(List<BusinessPartnerDTO> businessPartnerDTOS) {
        return businessPartnerDTOS.stream()
                .filter(bpn -> !bpn.getSupplier() && !bpn.getCustomer())
                .toList();
    }
}
