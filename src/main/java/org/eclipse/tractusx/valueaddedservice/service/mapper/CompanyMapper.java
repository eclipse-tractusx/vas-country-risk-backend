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
package org.eclipse.tractusx.valueaddedservice.service.mapper;

import org.eclipse.tractusx.valueaddedservice.domain.Company;
import org.eclipse.tractusx.valueaddedservice.domain.CompanyGroup;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyDTO;
import org.eclipse.tractusx.valueaddedservice.dto.CompanyGroupDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    @Mapping(target = "companyGroup", source = "companyGroup", qualifiedByName = "companyGroupId")
    CompanyDTO toDto(Company s);

    @Named("companyGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyGroupDTO toDtoCompanyGroupId(CompanyGroup companyGroup);
}
