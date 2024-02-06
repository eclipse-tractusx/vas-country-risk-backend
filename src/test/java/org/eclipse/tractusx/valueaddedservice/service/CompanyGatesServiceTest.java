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
package org.eclipse.tractusx.valueaddedservice.service;

import org.eclipse.tractusx.valueaddedservice.dto.CompanyGatesDTO;
import org.eclipse.tractusx.valueaddedservice.repository.CompanyGatesRepository;
import org.eclipse.tractusx.valueaddedservice.service.mapper.CompanyGatesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class CompanyGatesServiceTest {

    @Mock
    private CompanyGatesRepository companyGatesRepository;

    @Mock
    private CompanyGatesMapper companyGatesMapper;

    @InjectMocks
    private CompanyGatesService companyGatesService;



    @Test
    @DisplayName("Should save")
    void save() {
        CompanyGatesDTO companyGatesDTO = new CompanyGatesDTO();

        CompanyGatesDTO result = companyGatesService.save(companyGatesDTO);

        assertNotEquals(companyGatesDTO,result);
    }


}